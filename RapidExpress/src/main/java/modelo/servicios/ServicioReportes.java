package modelo.servicios;

import modelo.persistencia.IDaoReportes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServicioReportes {

    private final IDaoReportes daoReportes;

    public ServicioReportes(IDaoReportes daoReportes) {
        this.daoReportes = daoReportes;
    }

    public List<String> obtenerReporteEntregasPorConductor(String identificacionConductor, Date fechaInicio, Date fechaFin) {
        try {
            return daoReportes.obtenerEntregasPorConductor(identificacionConductor, fechaInicio, fechaFin);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al generar reporte de entregas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<String> obtenerHistorialRutasVehiculo(String placa) {
        try {
            return daoReportes.obtenerHistorialVehiculo(placa);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al obtener historial de rutas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
