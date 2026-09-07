package com.rapidexpress.model.dao;

import com.rapidexpress.model.entity.Clientes;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de persistencia para Clientes.
 */
public interface IDaoClientes {
    /** Inserta un nuevo cliente. */
    Clientes insertar(Clientes cliente) throws SQLException;
    /** Actualiza los datos de un cliente existente. */
    boolean actualizar(Clientes cliente) throws SQLException;
    /** Busca un cliente por su número de identificación. */
    Clientes obtenerPorIdentificacion(String identificacion) throws SQLException;
    /** Busca un cliente por su id interno. */
    Clientes obtenerPorId(int id) throws SQLException;
    /** Obtiene todos los clientes registrados. */
    List<Clientes> obtenerTodos() throws SQLException;
}
