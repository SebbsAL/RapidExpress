/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.model.dao;
import com.rapidexpress.model.entity.Mantenimientos;
import com.rapidexpress.model.entity.EstadoMantenimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Implementación JDBC del acceso a datos de Mantenimientos.
 *
 * @author sergi
 */
public class DaoMantenimientos implements IDaoMantenimientos {

     /** Inserta un nuevo mantenimiento. */
     public void insertar(Mantenimientos mantenimiento) throws SQLException {
        String sql = "INSERT INTO mantenimientos (vehiculo_id, tipo_mantenimiento, descripcion, fecha_programada, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mantenimiento.getVehiculoId());
            ps.setString(2, mantenimiento.getTipoMantenimiento());
            ps.setString(3, mantenimiento.getDescripcion());
            ps.setDate(4, java.sql.Date.valueOf(mantenimiento.getFechaProgramada()));
            ps.setString(5, mantenimiento.getEstado().name());
            ps.executeUpdate();
        }
    }

    /** Actualiza el estado, costo y observaciones de un mantenimiento. */
    public boolean actualizarEstadoYCostos(int idMantenimiento, EstadoMantenimiento estado, double costo, String observaciones) throws SQLException {
        String sql = "UPDATE mantenimientos SET estado=?, costo=?, observaciones=?, fecha_realizacion=CURRENT_DATE WHERE id=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setDouble(2, costo);
            ps.setString(3, observaciones);
            ps.setInt(4, idMantenimiento);
            return ps.executeUpdate() > 0;
        }
    }
    
    /** Cambia la fecha programada de un mantenimiento; retorna false si el id no existe. */
    public boolean actualizarFechaMantenimiento(int idMantenimiento, LocalDate fechaReAgendar) throws SQLException{
        String sql = "UPDATE mantenimientos SET fecha_programada = ? WHERE id = ?";
        try (Connection con = ConexionBD.MySQLConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setDate(1, java.sql.Date.valueOf(fechaReAgendar));
            ps.setInt(2, idMantenimiento);
            return ps.executeUpdate() > 0;
        }
    }    

    /** Busca un mantenimiento por su id. */
    public Mantenimientos obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM mantenimientos WHERE id=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearMantenimiento(rs);
            }
        }
        return null;
    }
    
    /** Obtiene el historial de mantenimientos de un vehículo por su placa. */
    public List<Mantenimientos> obtenerPorPlacaVehiculo(String placa) throws SQLException {
        String sql = "SELECT m.* FROM mantenimientos m JOIN vehiculos v ON m.vehiculo_id = v.id WHERE v.placa=?";
        List<Mantenimientos> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMantenimiento(rs));
                }
            }
        }
        return lista;
    }
    
    /** Obtiene todos los mantenimientos registrados, del mas reciente al mas antiguo. */
    public List<Mantenimientos> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM mantenimientos ORDER BY fecha_programada DESC";
        List<Mantenimientos> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearMantenimiento(rs));
            }
        }
        return lista;
    }

    /** Suma el costo de los mantenimientos COMPLETADOS de cada vehiculo, por placa. */
    public Map<String, Double> totalGastadoEnMantenimientos()throws SQLException{
        String sql = "SELECT vh.placa, SUM(mt.costo) AS total from vehiculos vh JOIN mantenimientos mt on vh.id = mt.vehiculo_id WHERE mt.estado='COMPLETADO' GROUP BY vh.placa;";
        LinkedHashMap<String, Double> gastoPorPlaca = new LinkedHashMap<>();
        try(Connection con = ConexionBD.MySQLConnection();PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                gastoPorPlaca.put(rs.getString("placa"), rs.getDouble("total"));
            }
        }
        return gastoPorPlaca;
    }

    /** Convierte una fila del ResultSet en un objeto Mantenimientos. */
    private Mantenimientos mapearMantenimiento(ResultSet rs) throws SQLException {
        Mantenimientos m = new Mantenimientos();
        m.setId(rs.getInt("id"));
        m.setVehiculoId(rs.getInt("vehiculo_id"));
        m.setTipoMantenimiento(rs.getString("tipo_mantenimiento"));
        m.setDescripcion(rs.getString("descripcion"));

        if (rs.getDate("fecha_programada") != null) {
            m.setFechaProgramada(rs.getDate("fecha_programada").toLocalDate());
        }
        if (rs.getDate("fecha_realizacion") != null) {
            m.setFechaRealizacion(rs.getDate("fecha_realizacion").toLocalDate());
        }

        m.setCosto(rs.getDouble("costo"));
        m.setEstado(EstadoMantenimiento.valueOf(rs.getString("estado")));
        m.setObservaciones(rs.getString("observaciones"));

        if (rs.getTimestamp("fecha_creacion") != null) {
            m.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        }
        return m;
    }
}
