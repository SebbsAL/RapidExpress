/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.controller;

import com.rapidexpress.model.entity.Clientes;
import com.rapidexpress.service.ServicioClientes;

/**
 *
 * @author sergi 
 */
public class ControladorClientes {
    private ServicioClientes servicioClientes;

    public ControladorClientes(ServicioClientes servicioClientes) {
        this.servicioClientes = servicioClientes;
    }
    
    /**
     * Registra un cliente nuevo o retorna el existente si ya está registrado.
     */
    public Clientes registrarBuscarCliente(String identificacion, String nombre, String telefono, String email, String direccion, String ciudad){
        return servicioClientes.registrarOObtenerCliente(identificacion, nombre, telefono, email, direccion, ciudad);
    }
}
