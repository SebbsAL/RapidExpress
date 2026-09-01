/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;
import modelo.clases.Rutas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author sergi
 */
public class DaoRutas {
    public int insertar(Rutas ruta) {
        String sql = "INSERT INTO rutas (codigo_ruta, vehiculo_id, conductor_id, fecha_ruta, peso_total_asignado_kg, estado) VALUES (?, ?, ?, ?, ?, ?)";
        int idGenerado = 0;
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ruta.getCodigoRuta());
            ps.setInt(2, ruta.getVehiculoId());
            ps.setInt(3, ruta.getConductorId());
            ps.setDate(4, java.sql.Date.valueOf(ruta.getFechaRuta()));
            ps.setDouble(5, ruta.getPesoTotalAsignadoKg());
            ps.setString(6, ruta.getEstado().name());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) idGenerado = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar ruta: " + e.getMessage());
        }
        return idGenerado;
    }

    public void actualizarEstado(String codigoRuta, String nuevoEstado) {
        String sql = "UPDATE rutas SET estado=?, ";
        if (nuevoEstado.equals("EN_PROCESO")) {
            sql += "hora_inicio=CURRENT_TIME ";
        } else if (nuevoEstado.equals("COMPLETADA")) {
            sql += "hora_fin=CURRENT_TIME ";
        } else {
            sql += "fecha_actualizacion=CURRENT_TIMESTAMP "; 
        }
        sql += "WHERE codigo_ruta=?";

        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, codigoRuta);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de ruta: " + e.getMessage());
        }
    }

    public List<Rutas> obtenerActivas() {
        String sql = "SELECT * FROM rutas WHERE estado IN ('PLANIFICADA', 'EN_PROCESO')";
        List<Rutas> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Rutas r = new Rutas();
                r.setId(rs.getInt("id"));
                r.setCodigoRuta(rs.getString("codigo_ruta"));
                r.setVehiculoId(rs.getInt("vehiculo_id"));
                r.setConductorId(rs.getInt("conductor_id"));
                
                if (rs.getDate("fecha_ruta") != null) {
                    r.setFechaRuta(rs.getDate("fecha_ruta").toLocalDate());
                }
                if (rs.getTime("hora_inicio") != null) {
                    r.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                }
                if (rs.getTime("hora_fin") != null) {
                    r.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                }
                
                r.setPesoTotalAsignadoKg(rs.getDouble("peso_total_asignado_kg"));
                r.setEstado(Rutas.Estado.valueOf(rs.getString("estado")));
                r.setObservaciones(rs.getString("observaciones"));
                
                // Si agregas setters a tu clase Rutas, puedes descomentar esto:
                // if (rs.getTimestamp("fecha_creacion") != null) r.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener rutas activas: " + e.getMessage());
        }
        return lista;
    }

    // --- Métodos de Ruta Paquetes (ruta_paquetes) ---

    public void asociarPaqueteARuta(int rutaId, int paqueteId, int ordenEntrega) {
        String sql = "INSERT INTO ruta_paquetes (ruta_id, paquete_id, orden_entrega, estado_entrega) VALUES (?, ?, ?, 'PENDIENTE')";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rutaId);
            ps.setInt(2, paqueteId);
            ps.setInt(3, ordenEntrega);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al asociar paquete a ruta: " + e.getMessage());
        }
    }

    public void actualizarEstadoEntregaPaquete(String codigoRuta, String codigoSeguimiento, String estadoEntrega, String observaciones) {
        String sql = "UPDATE ruta_paquetes rp " +
                     "JOIN rutas r ON rp.ruta_id = r.id " +
                     "JOIN paquetes p ON rp.paquete_id = p.id " +
                     "SET rp.estado_entrega=?, rp.observaciones_entrega=?, rp.fecha_entrega_real=CURRENT_TIMESTAMP " +
                     "WHERE r.codigo_ruta=? AND p.codigo_seguimiento=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estadoEntrega);
            ps.setString(2, observaciones);
            ps.setString(3, codigoRuta);
            ps.setString(4, codigoSeguimiento);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualizando entrega de paquete: " + e.getMessage());
        }
    }
}
