package com.rapidexpress.model.dao;

import com.rapidexpress.model.entity.Conductores;
import com.rapidexpress.model.entity.EstadoConductor;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de persistencia para Conductores.
 */
public interface IDaoConductores {
    /** Inserta un nuevo conductor. */
    void insertar(Conductores conductor) throws SQLException;
    /** Actualiza los datos de un conductor existente. */
    boolean actualizar(Conductores conductor) throws SQLException;
    /** Actualiza el estado de un conductor. */
    boolean actualizarEstado(String identificacion, EstadoConductor nuevoEstado) throws SQLException;
    /** Busca un conductor por su número de identificación. */
    Conductores obtenerPorIdentificacion(String identificacion) throws SQLException;
    /** Busca un conductor por su id interno. */
    Conductores obtenerPorId(int id) throws SQLException;
    /** Obtiene todos los conductores registrados. */
    List<Conductores> obtenerTodos() throws SQLException;
    /** Indica si el conductor tiene una asignación de vehículo activa. */
    boolean tieneAsignacionActiva(String identificacionConductor) throws SQLException;
    /** Indica si el vehículo ya tiene un conductor con asignación activa. */
    boolean vehiculoTieneAsignacionActiva(int idVehiculo) throws SQLException;
    /** Registra la asignación de un vehículo a un conductor. */
    void registrarAsignacion(int idVehiculo, int idConductor) throws SQLException;
    /** Cierra (desactiva) la asignación de vehículo activa de un conductor. */
    boolean desasignarVehiculo(String identificacionConductor) throws SQLException;
    
    List<Conductores> conductoresSinVehiculoAsignado() throws SQLException;
}
