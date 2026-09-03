/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;
import modelo.clases.Mantenimientos;
import modelo.clases.EstadoMantenimiento;
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
public class DaoMantenimientos {
    
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

    public List<Mantenimientos> obtenerPorPlacaVehiculo(String placa) throws SQLException {
        String sql = "SELECT m.* FROM mantenimientos m JOIN vehiculos v ON m.vehiculo_id = v.id WHERE v.placa=?";
        List<Mantenimientos> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
                    lista.add(m);
                }
            }
        }
        return lista;
    }
}
