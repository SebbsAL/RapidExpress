package com.rapidexpress.model.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Contrato de persistencia para la generacion de reportes operativos.
 */
public interface IDaoReportes {
    /** Obtiene las entregas realizadas por un conductor en un rango de fechas. */
    List<String> obtenerEntregasPorConductor(String identificacionConductor, Date fechaInicio, Date fechaFin) throws SQLException;
    /** Obtiene el historial de rutas de un vehículo. */
    List<String> obtenerHistorialVehiculo(String placa) throws SQLException;
}
