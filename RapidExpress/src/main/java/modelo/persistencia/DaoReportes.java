/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author sergi
 */
public class DaoReportes {
    public List<String> obtenerEntregasPorConductor(String identificacionConductor, Date fechaInicio, Date fechaFin) {
        String sql = "SELECT * FROM vista_reporte_entregas_conductor WHERE numero_identificacion = ? AND fecha_ruta BETWEEN ? AND ?";
        List<String> lineas = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, identificacionConductor);
            ps.setDate(2, new java.sql.Date(fechaInicio.getTime()));
            ps.setDate(3, new java.sql.Date(fechaFin.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lineas.add("Conductor: " + rs.getString("conductor_nombre") +
                                       " | Ruta: " + rs.getString("codigo_ruta") +
                                       " | Paquete: " + rs.getString("codigo_seguimiento") +
                                       " | Estado: " + rs.getString("estado_entrega"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generando reporte: " + e.getMessage());
        }
        return lineas;
    }

    public List<String> obtenerHistorialVehiculo(String placa) {
        String sql = "SELECT * FROM vista_historial_vehiculos WHERE placa = ?";
        List<String> lineas = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lineas.add("Placa: " + rs.getString("placa"));
                    lineas.add("Rutas Realizadas: " + rs.getInt("total_rutas_realizadas"));
                    lineas.add("KG Transportados: " + rs.getDouble("total_kg_transportados"));
                    lineas.add("Mantenimientos: " + rs.getInt("total_mantenimientos_registrados"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error consultando historial de vehiculos: " + e.getMessage());
        }
        return lineas;
    }
}
