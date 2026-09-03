package com.rapidexpress.service;

import com.rapidexpress.model.entity.Clientes;
import com.rapidexpress.model.dao.IDaoClientes;
import java.sql.SQLException;

/**
 * Servicio de gestión de clientes (registro y consulta).
 */
public class ServicioClientes {

    private final IDaoClientes daoClientes;

    public ServicioClientes(IDaoClientes daoClientes) {
        this.daoClientes = daoClientes;
    }

    /**
     * Registra un cliente nuevo o retorna el existente si ya está registrado.
     */
    public Clientes registrarOObtenerCliente(String identificacion, String nombre, String telefono, String email, String direccion, String ciudad) {
        try {
            Clientes existente = daoClientes.obtenerPorIdentificacion(identificacion);
            if (existente != null) {
                return existente;
            }

            Clientes nuevo = new Clientes();
            nuevo.setNumeroIdentificacion(identificacion);
            nuevo.setNombreCompleto(nombre);
            nuevo.setTelefono(telefono);
            nuevo.setEmail(email);
            nuevo.setDireccion(direccion);
            nuevo.setCiudad(ciudad);

            return daoClientes.insertar(nuevo);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al registrar/obtener cliente: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene un cliente a partir de su identificador interno.
     */
    public Clientes obtenerClientePorId(int id) {
        try {
            return daoClientes.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al obtener cliente: " + e.getMessage());
            return null;
        }
    }
}
