/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.controller;

import com.rapidexpress.model.entity.Clientes;
import com.rapidexpress.service.ServicioClientes;
import java.util.List;

/**
 *
 * @author sergi
 */
public class ControladorClientes {
    private static final String USUARIO_SISTEMA = "SISTEMA";
    private ServicioClientes servicioClientes;
    private ControladorAuditoria controladorAuditoria;

    public ControladorClientes(ServicioClientes servicioClientes, ControladorAuditoria controladorAuditoria) {
        this.servicioClientes = servicioClientes;
        this.controladorAuditoria = controladorAuditoria;
    }

    /**
     * Registra un cliente nuevo o retorna el existente si ya está registrado.
     * Solo se audita cuando el cliente realmente se creó: si ya existía, esto es
     * una consulta, no un alta, y registrarla como CREACION seria falso.
     */
    public Clientes registrarBuscarCliente(String identificacion, String nombre, String telefono, String email, String direccion, String ciudad){
        boolean yaExistia = servicioClientes.buscarClientePorIdentificacion(identificacion) != null;
        Clientes cliente = servicioClientes.registrarOObtenerCliente(identificacion, nombre, telefono, email, direccion, ciudad);
        if (cliente != null && !yaExistia) {
            controladorAuditoria.registrar("CLIENTES", "CREACION", "Cliente registrado: " + identificacion, USUARIO_SISTEMA);
        }
        return cliente;
    }

    /**
     * Actualiza los datos de un cliente existente y deja constancia en la auditoría.
     */
    public boolean actualizarDatosCliente(String identificacion, String nombre, String telefono, String email, String direccion, String ciudad){
        boolean exito = servicioClientes.actualizarDatosCliente(identificacion, nombre, telefono, email, direccion, ciudad);
        if (exito) {
            controladorAuditoria.registrar("CLIENTES", "ACTUALIZACION", "Cliente actualizado: " + identificacion, USUARIO_SISTEMA);
        }
        return exito;
    }

    /**
     * Lista todos los clientes registrados.
     */
    public List<Clientes> listarClientes(){
        return servicioClientes.listarClientes();
    }

    /**
     * Busca un cliente por su número de identificación.
     */
    public Clientes buscarClientePorIdentificacion(String identificacion){
        return servicioClientes.buscarClientePorIdentificacion(identificacion);
    }
}
