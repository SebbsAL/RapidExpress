/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.model.dao;
import com.rapidexpress.model.entity.Rutas;
import com.rapidexpress.model.entity.EstadoRuta;
import com.rapidexpress.model.entity.RutaPaquetes;
import com.rapidexpress.model.entity.EstadoEntrega;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementación JDBC del acceso a datos de Rutas y ruta_paquetes.
 *
 * @author sergi
 */
public class DaoRutas implements IDaoRutas {
    /** Inserta una nueva ruta y devuelve su id generado. */
    public int insertar(Rutas ruta) throws SQLException {
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
        }
        return idGenerado;
    }

    /** Actualiza el estado de una ruta, registrando hora de inicio o fin según corresponda. */
    public void actualizarEstado(String codigoRuta, EstadoRuta nuevoEstado) throws SQLException {
        String sql = "UPDATE rutas SET estado=?, ";
        if (nuevoEstado == EstadoRuta.EN_PROCESO) {
            sql += "hora_inicio=CURRENT_TIME ";
        } else if (nuevoEstado == EstadoRuta.COMPLETADA) {
            sql += "hora_fin=CURRENT_TIME ";
        } else {
            sql += "fecha_actualizacion=CURRENT_TIMESTAMP ";
        }
        sql += "WHERE codigo_ruta=?";

        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, codigoRuta);
            ps.executeUpdate();
        }
    }

    /** Obtiene las rutas en estado PLANIFICADA o EN_PROCESO. */
    public List<Rutas> obtenerActivas() throws SQLException {
        String sql = "SELECT * FROM rutas WHERE estado IN ('PLANIFICADA', 'EN_PROCESO')";
        List<Rutas> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearRuta(rs));
            }
        }
        return lista;
    }
    
    /** Obtiene todas las rutas en estado COMPLETADA. */
    public List<Rutas> detallesRutasCompletadas() throws SQLException{
        List<Rutas> lista = new ArrayList<>();
        String sql = "SELECT * FROM rutas WHERE estado='COMPLETADA'";
        try(Connection con = ConexionBD.MySQLConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                lista.add(mapearRuta(rs));
            }
        }
        return lista;
    }
    
    /** Busca una ruta por su código. */
    public Rutas obtenerPorCodigo(String codigoRuta) throws SQLException {
        String sql = "SELECT * FROM rutas WHERE codigo_ruta=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoRuta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearRuta(rs);
            }
        }
        return null;
    }

    /** Convierte una fila del ResultSet en un objeto Rutas. */
    private Rutas mapearRuta(ResultSet rs) throws SQLException {
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
        r.setEstado(EstadoRuta.valueOf(rs.getString("estado")));
        r.setObservaciones(rs.getString("observaciones"));

        return r;
    }

    // --- Métodos de Ruta Paquetes (ruta_paquetes) ---

    /** Asocia un paquete a una ruta con su orden de entrega, en estado PENDIENTE. */
    public void asociarPaqueteARuta(int rutaId, int paqueteId, int ordenEntrega) throws SQLException {
        String sql = "INSERT INTO ruta_paquetes (ruta_id, paquete_id, orden_entrega, estado_entrega) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rutaId);
            ps.setInt(2, paqueteId);
            ps.setInt(3, ordenEntrega);
            ps.setString(4, EstadoEntrega.PENDIENTE.name());
            ps.executeUpdate();
        }
    }

    /** Actualiza el estado de entrega de un paquete dentro de una ruta. */
    public boolean actualizarEstadoEntregaPaquete(String codigoRuta, String codigoSeguimiento, EstadoEntrega estadoEntrega, String observaciones) throws SQLException {
        String sql = "UPDATE ruta_paquetes rp " +
                     "JOIN rutas r ON rp.ruta_id = r.id " +
                     "JOIN paquetes p ON rp.paquete_id = p.id " +
                     "SET rp.estado_entrega=?, rp.observaciones_entrega=?, rp.fecha_entrega_real=CURRENT_TIMESTAMP " +
                     "WHERE r.codigo_ruta=? AND p.codigo_seguimiento=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estadoEntrega.name());
            ps.setString(2, observaciones);
            ps.setString(3, codigoRuta);
            ps.setString(4, codigoSeguimiento);
            return ps.executeUpdate() > 0;
        }
    }

    /** Obtiene el detalle de entregas de una ruta, en orden de entrega. */
    public List<RutaPaquetes> obtenerDetalleEntregas(int rutaId) throws SQLException {
        String sql = "SELECT * FROM ruta_paquetes WHERE ruta_id=? ORDER BY orden_entrega";
        List<RutaPaquetes> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rutaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RutaPaquetes rp = new RutaPaquetes();
                    rp.setId(rs.getInt("id"));
                    rp.setRutaId(rs.getInt("ruta_id"));
                    rp.setPaqueteId(rs.getInt("paquete_id"));
                    rp.setOrdenEntrega(rs.getInt("orden_entrega"));
                    rp.setEstadoEntrega(EstadoEntrega.valueOf(rs.getString("estado_entrega")));
                    if (rs.getTimestamp("fecha_entrega_estimada") != null) {
                        rp.setFechaEntregaEstimada(rs.getTimestamp("fecha_entrega_estimada").toLocalDateTime());
                    }
                    if (rs.getTimestamp("fecha_entrega_real") != null) {
                        rp.setFechaEntregaReal(rs.getTimestamp("fecha_entrega_real").toLocalDateTime());
                    }
                    rp.setObservacionesEntrega(rs.getString("observaciones_entrega"));
                    lista.add(rp);
                }
            }
        }
        return lista;
    }
}
