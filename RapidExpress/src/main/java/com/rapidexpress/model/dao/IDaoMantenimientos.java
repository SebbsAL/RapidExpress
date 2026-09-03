package modelo.persistencia;

import modelo.clases.EstadoMantenimiento;
import modelo.clases.Mantenimientos;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de persistencia para Mantenimientos de vehiculos.
 */
public interface IDaoMantenimientos {
    void insertar(Mantenimientos mantenimiento) throws SQLException;
    boolean actualizarEstadoYCostos(int idMantenimiento, EstadoMantenimiento estado, double costo, String observaciones) throws SQLException;
    List<Mantenimientos> obtenerPorPlacaVehiculo(String placa) throws SQLException;
}
