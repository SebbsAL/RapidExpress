package com.rapidexpress.model.dao;

import com.rapidexpress.model.entity.EstadoMantenimiento;
import com.rapidexpress.model.entity.Mantenimientos;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Contrato de persistencia para Mantenimientos de vehiculos.
 */
public interface IDaoMantenimientos {
    /** Inserta un nuevo mantenimiento. */
    void insertar(Mantenimientos mantenimiento) throws SQLException;
    /** Actualiza el estado, costo y observaciones de un mantenimiento. */
    boolean actualizarEstadoYCostos(int idMantenimiento, EstadoMantenimiento estado, double costo, String observaciones) throws SQLException;
    /** Busca un mantenimiento por su id. */
    Mantenimientos obtenerPorId(int id) throws SQLException;
    /** Obtiene el historial de mantenimientos de un vehículo por su placa. */
    List<Mantenimientos> obtenerPorPlacaVehiculo(String placa) throws SQLException;
    
    Map<String, Double> totalGastadoEnMantenimientos()throws SQLException;
}
