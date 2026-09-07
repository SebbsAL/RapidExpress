package com.rapidexpress.service;

import com.rapidexpress.model.entity.Clientes;
import com.rapidexpress.model.dao.IDaoClientes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
     * Actualiza los datos de un cliente existente, identificado por su número de identificación.
     * El número de identificación no se modifica: es la llave con la que se ubica al cliente.
     */
    public boolean actualizarDatosCliente(String identificacion, String nombre, String telefono, String email, String direccion, String ciudad) {
        try {
            Clientes existente = daoClientes.obtenerPorIdentificacion(identificacion);
            if (existente == null) {
                System.err.println("Error: No se encontro el cliente con identificacion " + identificacion);
                return false;
            }

            Clientes cliente = new Clientes();
            cliente.setNumeroIdentificacion(identificacion);
            cliente.setNombreCompleto(nombre);
            cliente.setTelefono(telefono);
            cliente.setEmail(email);
            cliente.setDireccion(direccion);
            cliente.setCiudad(ciudad);

            boolean actualizado = daoClientes.actualizar(cliente);
            if (!actualizado) {
                System.err.println("Error: No se pudo actualizar el cliente " + identificacion);
                return false;
            }
            System.out.println("Datos del cliente actualizados: " + identificacion);
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos los clientes registrados, ordenados por nombre.
     */
    public List<Clientes> listarClientes() {
        try {
            List<Clientes> clientes = daoClientes.obtenerTodos();
            clientes.sort(Comparator.comparing(Clientes::getNombreCompleto));
            return clientes;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al listar clientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca un cliente por su número de identificación, sin crearlo si no existe.
     */
    public Clientes buscarClientePorIdentificacion(String identificacion) {
        try {
            return daoClientes.obtenerPorIdentificacion(identificacion);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al buscar cliente: " + e.getMessage());
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
