/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;
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
}
