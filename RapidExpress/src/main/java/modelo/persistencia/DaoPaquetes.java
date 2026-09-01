/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;
import modelo.clases.Paquetes;
import modelo.clases.HistorialPaquetes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author sergi
 */
public class DaoPaquetes {
    public void insertar(Paquetes paquete) {
        String sql = "INSERT INTO paquetes (codigo_seguimiento, descripcion_contenido, peso_kg, largo_cm, ancho_cm, alto_cm, volumen_m3, direccion_origen, direccion_destino, remitente_id, destinatario_id, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, paquete.getCodigoSeguimiento());
            ps.setString(2, paquete.getDescripcionContenido());
            ps.setDouble(3, paquete.getPesoKg());
            ps.setDouble(4, paquete.getLargoCm());
            ps.setDouble(5, paquete.getAnchoCm());
            ps.setDouble(6, paquete.getAltoCm());
            ps.setDouble(7, paquete.getVolumenM3());
            ps.setString(8, paquete.getDireccionOrigen());
            ps.setString(9, paquete.getDireccionDestino());
            ps.setInt(10, paquete.getRemitenteId());
            ps.setInt(11, paquete.getDestinatatioId()); // Nombre de tu clase (con typo original)
            ps.setString(12, paquete.getEstado().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar paquete: " + e.getMessage());
        }
    }

    public void actualizarEstado(String codigoSeguimiento, String nuevoEstado) {
        String sql = "UPDATE paquetes SET estado=? WHERE codigo_seguimiento=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, codigoSeguimiento);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del paquete: " + e.getMessage());
        }
    }

    public Paquetes obtenerPorTracking(String codigoSeguimiento) {
        String sql = "SELECT * FROM paquetes WHERE codigo_seguimiento=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoSeguimiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPaquete(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paquete: " + e.getMessage());
        }
        return null;
    }

    public List<Paquetes> obtenerPorEstado(String estado) {
        String sql = "SELECT * FROM paquetes WHERE estado=?";
        List<Paquetes> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearPaquete(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paquetes por estado: " + e.getMessage());
        }
        return lista;
    }

    private Paquetes mapearPaquete(ResultSet rs) throws SQLException {
        Paquetes p = new Paquetes();
        p.setId(rs.getInt("id"));
        p.setCodigoSeguimiento(rs.getString("codigo_seguimiento"));
        p.setDescripcionContenido(rs.getString("descripcion_contenido"));
        p.setPesoKg(rs.getDouble("peso_kg"));
        p.setLargoCm(rs.getDouble("largo_cm"));
        p.setAnchoCm(rs.getDouble("ancho_cm"));
        p.setAltoCm(rs.getDouble("alto_cm"));
        p.setVolumenM3(rs.getDouble("volumen_m3"));
        p.setDireccionOrigen(rs.getString("direccion_origen"));
        p.setDireccionDestino(rs.getString("direccion_destino"));
        p.setRemitenteId(rs.getInt("remitente_id"));
        
        // Uso de nombre idéntico a la clase
        p.setDestinatatioId(rs.getInt("destinatario_id")); 
        
        p.setEstado(Paquetes.Estado.valueOf(rs.getString("estado")));
        
        if (rs.getTimestamp("fecha_creacion") != null) {
            p.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        }
        if (rs.getTimestamp("fecha_actualizacion") != null) {
            p.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion").toLocalDateTime());
        }
        return p;
    }

    // --- Métodos de Historial (historial_paquetes) ---

    public void registrarHistorial(HistorialPaquetes historial) {
        String sql = "INSERT INTO historial_paquetes (paquete_id, estado, descripcion_evento, ubicacion) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, historial.getPaqueteId());
            ps.setString(2, historial.getEstado().name());
            ps.setString(3, historial.getDescripcionEvento());
            ps.setString(4, historial.getUbicacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar historial: " + e.getMessage());
        }
    }

    public List<HistorialPaquetes> obtenerHistorial(String codigoSeguimiento) {
        String sql = "SELECT h.* FROM historial_paquetes h JOIN paquetes p ON h.paquete_id = p.id WHERE p.codigo_seguimiento=? ORDER BY h.fecha_registro DESC";
        List<HistorialPaquetes> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoSeguimiento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HistorialPaquetes h = new HistorialPaquetes();
                    h.setId(rs.getInt("id"));
                    h.setPaqueteId(rs.getInt("paquete_id"));
                    h.setEstado(HistorialPaquetes.Estado.valueOf(rs.getString("estado")));
                    h.setDescripcionEvento(rs.getString("descripcion_evento"));
                    h.setUbicacion(rs.getString("ubicacion"));
                    if (rs.getTimestamp("fecha_registro") != null) {
                        // En tu clase no existe setter para fechaRegistro, deberás agregarlo si deseas poblarlo aquí
                        // h.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                    }
                    lista.add(h);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
        }
        return lista;
    }
}
