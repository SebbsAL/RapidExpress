package modelo.persistencia;

import modelo.clases.Conductores;
import modelo.clases.EstadoConductor;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de persistencia para Conductores.
 */
public interface IDaoConductores {
    void insertar(Conductores conductor) throws SQLException;
    boolean actualizar(Conductores conductor) throws SQLException;
    boolean actualizarEstado(String identificacion, EstadoConductor nuevoEstado) throws SQLException;
    Conductores obtenerPorIdentificacion(String identificacion) throws SQLException;
    Conductores obtenerPorId(int id) throws SQLException;
    List<Conductores> obtenerTodos() throws SQLException;
    boolean tieneAsignacionActiva(String identificacionConductor) throws SQLException;
    void registrarAsignacion(int idVehiculo, int idConductor) throws SQLException;
}
