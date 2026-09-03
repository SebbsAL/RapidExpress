package modelo.persistencia;

import modelo.clases.EstadoVehiculo;
import modelo.clases.Vehiculos;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de persistencia para Vehiculos. Permite que la capa de Servicios
 * dependa de una abstraccion en vez de la implementacion concreta (DIP).
 */
public interface IDaoVehiculos {
    void insertar(Vehiculos vehiculo) throws SQLException;
    boolean actualizar(Vehiculos vehiculo) throws SQLException;
    void actualizarEstado(String placa, EstadoVehiculo nuevoEstado) throws SQLException;
    Vehiculos obtenerPorPlaca(String placa) throws SQLException;
    Vehiculos obtenerPorId(int id) throws SQLException;
    List<Vehiculos> obtenerTodos() throws SQLException;
}
