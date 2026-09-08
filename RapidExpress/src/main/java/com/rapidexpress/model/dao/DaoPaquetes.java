/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.model.dao;
import com.rapidexpress.model.entity.Paquetes;
import com.rapidexpress.model.entity.EstadoPaquete;
import com.rapidexpress.model.entity.HistorialPaquetes;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Implementación JDBC del acceso a datos de Paquetes y su historial.
 *
 * @author sergi
 */
public class DaoPaquetes implements IDaoPaquetes {
    /** Inserta un nuevo paquete. */
    public void insertar(Paquetes paquete) throws SQLException {
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
            ps.setInt(11, paquete.getDestinatarioId());
            ps.setString(12, paquete.getEstado().name());
            ps.executeUpdate();
        }
    }

    /** Actualiza el estado de un paquete. */
    public void actualizarEstado(String codigoSeguimiento, EstadoPaquete nuevoEstado) throws SQLException {
        String sql = "UPDATE paquetes SET estado=? WHERE codigo_seguimiento=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, codigoSeguimiento);
            ps.executeUpdate();
        }
    }

    /** Busca un paquete por su código de seguimiento. */
    public Paquetes obtenerPorTracking(String codigoSeguimiento) throws SQLException {
        String sql = "SELECT * FROM paquetes WHERE codigo_seguimiento=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoSeguimiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPaquete(rs);
            }
        }
        return null;
    }

    /** Busca un paquete por su id interno. */
    public Paquetes obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM paquetes WHERE id=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPaquete(rs);
            }
        }
        return null;
    }

    /** Obtiene los paquetes que están en un estado dado. */
    public List<Paquetes> obtenerPorEstado(String estado) throws SQLException {
        String sql = "SELECT * FROM paquetes WHERE estado=?";
        List<Paquetes> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearPaquete(rs));
            }
        }
        return lista;
    }
    
    /** Obtiene los paquetes asignados a una ruta, en orden de entrega. */
    public List<Paquetes> obtenerPorRuta(int rutaId) throws SQLException {
        String sql = "SELECT p.* FROM paquetes p JOIN ruta_paquetes rp ON p.id = rp.paquete_id WHERE rp.ruta_id=? ORDER BY rp.orden_entrega";
        List<Paquetes> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rutaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearPaquete(rs));
            }
        }
        return lista;
    }

    /** Convierte una fila del ResultSet en un objeto Paquetes. */
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
        p.setDestinatarioId(rs.getInt("destinatario_id"));

        p.setEstado(EstadoPaquete.valueOf(rs.getString("estado")));

        if (rs.getTimestamp("fecha_creacion") != null) {
            p.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        }
        if (rs.getTimestamp("fecha_actualizacion") != null) {
            p.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion").toLocalDateTime());
        }
        return p;
    }

    // --- Métodos de Historial (historial_paquetes) ---

    /** Registra un evento en el historial de un paquete. */
    public void registrarHistorial(HistorialPaquetes historial) throws SQLException {
        String sql = "INSERT INTO historial_paquetes (paquete_id, estado, descripcion_evento, ubicacion) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, historial.getPaqueteId());
            ps.setString(2, historial.getEstado().name());
            ps.setString(3, historial.getDescripcionEvento());
            ps.setString(4, historial.getUbicacion());
            ps.executeUpdate();
        }
    }

    /** Obtiene el historial de eventos de un paquete, del más reciente al más antiguo. */
    public List<HistorialPaquetes> obtenerHistorial(String codigoSeguimiento) throws SQLException {
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
                    h.setEstado(EstadoPaquete.valueOf(rs.getString("estado")));
                    h.setDescripcionEvento(rs.getString("descripcion_evento"));
                    h.setUbicacion(rs.getString("ubicacion"));
                    if (rs.getTimestamp("fecha_registro") != null) {
                        h.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                    }
                    lista.add(h);
                }
            }
        }
        return lista;
    }
    
    /** Busca los paquetes cuya ultima actualizacion tiene N o mas dias de antiguedad. */
    public List<Paquetes> buscarPorDiasSinActualizar(int diasSinActualizar) throws SQLException{
        String sql = "SELECT * FROM paquetes WHERE fecha_actualizacion <= NOW() - INTERVAL ? DAY";
        List<Paquetes> lista = new ArrayList<>();
        try(Connection con = ConexionBD.MySQLConnection(); PreparedStatement PS = con.prepareStatement(sql)){
            PS.setInt(1, diasSinActualizar);
            try(ResultSet RS = PS.executeQuery()){
                while (RS.next()) {lista.add(mapearPaquete(RS));}
            }
        }
        return lista;
    }

    
    /** Cuenta los envios de cada remitente, solo de los que tienen 3 o mas. */
    public Map<String, Integer> contarPedidosPorRemitente() throws SQLException {
        String sql = " SELECT c.nombre_completo, COUNT(*) AS total FROM paquetes p JOIN clientes c ON p.remitente_id = c.id GROUP BY c.id HAVING COUNT(*) >= 3";
        LinkedHashMap<String, Integer> mapa = new LinkedHashMap<>();
        try(Connection con = ConexionBD.MySQLConnection(); PreparedStatement PS = con.prepareStatement(sql); ResultSet RS = PS.executeQuery()){
            while (RS.next()) {mapa.put(RS.getString("nombre_completo"), RS.getInt("total"));}
        }
        return mapa;
    }
    
    /**
     * Cuenta los paquetes registrados entre dos fechas, agrupados por estado.
     * Se usa DATE(fecha_creacion) porque la columna es DATETIME: sin eso, los
     * paquetes creados el ultimo dia del rango despues de medianoche quedarian fuera.
     */
    public Map<String, Integer> paquetesRegistradosEnRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) throws SQLException{
        String sql = "SELECT estado, COUNT(*) AS paquetes_registrados FROM paquetes WHERE DATE(fecha_creacion) BETWEEN ? AND ? GROUP BY estado";
        LinkedHashMap<String, Integer> mapa = new LinkedHashMap<>();
        try (Connection con = ConexionBD.MySQLConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {mapa.put(rs.getString("estado"),rs.getInt("paquetes_registrados"));}
            }
        }
        return mapa;
    }

    /** Cuenta cuántos paquetes ha enviado (como remitente) el cliente con esa identificación. */
    public int contarEnviadosPorIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM paquetes p JOIN clientes c ON p.remitente_id = c.id WHERE c.numero_identificacion = ?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        }
        return 0;
    }

    /** Cuenta cuántos paquetes ha recibido (como destinatario) el cliente con esa identificación. */
    public int contarRecibidosPorIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM paquetes p JOIN clientes c ON p.destinatario_id = c.id WHERE c.numero_identificacion = ?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        }
        return 0;
    }
}