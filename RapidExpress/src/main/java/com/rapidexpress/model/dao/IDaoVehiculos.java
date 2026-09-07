package com.rapidexpress.model.dao;

import com.rapidexpress.model.entity.EstadoVehiculo;
import com.rapidexpress.model.entity.Vehiculos;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Contrato de persistencia para Vehiculos. Permite que la capa de Servicios
 * dependa de una abstraccion en vez de la implementacion concreta (DIP).
 */
public interface IDaoVehiculos {
    /** Inserta un nuevo vehículo. */
    void insertar(Vehiculos vehiculo) throws SQLException;
    /** Actualiza los datos de un vehículo existente. */
    boolean actualizar(Vehiculos vehiculo) throws SQLException;
    /** Actualiza el estado de un vehículo. */
    void actualizarEstado(String placa, EstadoVehiculo nuevoEstado) throws SQLException;
    /** Busca un vehículo por su placa. */
    Vehiculos obtenerPorPlaca(String placa) throws SQLException;
    /** Busca un vehículo por su id interno. */
    Vehiculos obtenerPorId(int id) throws SQLException;
    /** Obtiene todos los vehículos registrados. */
    List<Vehiculos> obtenerTodos() throws SQLException;
    
    List<Vehiculos> ListarVehiculoPorAptitudes(double kg) throws SQLException;
    
    Map<String, Integer> ContabilizarEstados() throws SQLException;
}
