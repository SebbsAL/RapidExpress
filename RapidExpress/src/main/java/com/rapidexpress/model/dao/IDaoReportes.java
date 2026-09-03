package modelo.persistencia;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Contrato de persistencia para la generacion de reportes operativos.
 */
public interface IDaoReportes {
    List<String> obtenerEntregasPorConductor(String identificacionConductor, Date fechaInicio, Date fechaFin) throws SQLException;
    List<String> obtenerHistorialVehiculo(String placa) throws SQLException;
}
