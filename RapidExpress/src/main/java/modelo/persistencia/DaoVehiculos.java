/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;
import modelo.clases.EstadoVehiculo;
import modelo.clases.Vehiculos;
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
public class DaoVehiculos {
    public void insertar(Vehiculos vehiculo) {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, anio_fabricacion, capacidad_maxima_kg, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio_fabricacion());
            ps.setDouble(5, vehiculo.getCapacidad_maxima_kg());
            ps.setString(6, vehiculo.getEstado().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar vehículo: " + e.getMessage());
        }
    }
    
    public void actualizar(Vehiculos vehiculo) {
        String sql = "UPDATE vehiculos SET marca=?, modelo=?, anio_fabricacion=?, capacidad_maxima_kg=? WHERE placa=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehiculo.getMarca());
            ps.setString(2, vehiculo.getModelo());
            ps.setInt(3, vehiculo.getAnio_fabricacion());
            ps.setDouble(4, vehiculo.getCapacidad_maxima_kg());
            ps.setString(5, vehiculo.getPlaca());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar vehículo: " + e.getMessage());
        }
    }
    
    public void actualizarEstado(String placa, EstadoVehiculo nuevoEstado) {
        String sql = "UPDATE vehiculos SET estado=? WHERE placa=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, placa);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del vehículo: " + e.getMessage());
        }
    }

    public Vehiculos obtenerPorPlaca(String placa) {
        String sql = "SELECT * FROM vehiculos WHERE placa=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearVehiculo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener vehículo: " + e.getMessage());
        }
        return null;
    }
    

    public Vehiculos obtenerPorId(int id) {
        String sql = "SELECT * FROM vehiculos WHERE id=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearVehiculo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener vehículo: " + e.getMessage());
        }
        return null;
    }

    public List<Vehiculos> obtenerTodos() {
        String sql = "SELECT * FROM vehiculos";
        List<Vehiculos> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearVehiculo(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los vehículos: " + e.getMessage());
        }
        return lista;
    }
    

    private Vehiculos mapearVehiculo(ResultSet rs) throws SQLException {
        Vehiculos v = new Vehiculos();
        v.setId(rs.getInt("id"));
        v.setPlaca(rs.getString("placa"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setAnio_fabricacion(rs.getInt("anio_fabricacion"));
        v.setCapacidad_maxima_kg(rs.getInt("capacidad_maxima_kg")); // Basado en tu clase actual
        v.setEstado(EstadoVehiculo.valueOf(rs.getString("estado")));
        if (rs.getTimestamp("fecha_creacion") != null) {
            v.setFecha_creacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        }
        if (rs.getTimestamp("fecha_actualizacion") != null) {
            v.setFecha_actualizacion(rs.getTimestamp("fecha_actualizacion").toLocalDateTime());
        }
        return v;
    }
}
