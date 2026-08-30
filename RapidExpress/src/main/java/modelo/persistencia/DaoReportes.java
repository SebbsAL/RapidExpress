/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
/**
 *
 * @author sergi
 */
public class DaoReportes {
    public void imprimirEntregasPorConductor(Date fechaInicio, Date fechaFin) {
        String sql = "SELECT * FROM vista_reporte_entregas_conductor WHERE fecha_ruta BETWEEN ? AND ?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            ps.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            System.out.println("--- REPORTE DE ENTREGAS ---");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Conductor: " + rs.getString("conductor_nombre") + 
                                       " | Ruta: " + rs.getString("codigo_ruta") + 
                                       " | Paquete: " + rs.getString("codigo_seguimiento") + 
                                       " | Estado: " + rs.getString("estado_entrega"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generando reporte: " + e.getMessage());
        }
    }

    public void imprimirHistorialVehiculos(String placa) {
        String sql = "SELECT * FROM vista_historial_vehiculos WHERE placa = ?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            
            System.out.println("--- HISTORIAL DE VEHÍCULO ---");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Placa: " + rs.getString("placa"));
                    System.out.println("Rutas Realizadas: " + rs.getInt("total_rutas_realizadas"));
                    System.out.println("KG Transportados: " + rs.getDouble("total_kg_transportados"));
                    System.out.println("Mantenimientos: " + rs.getInt("total_mantenimientos_registrados"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error consultando historial de vehículos: " + e.getMessage());
        }
    } 
}
