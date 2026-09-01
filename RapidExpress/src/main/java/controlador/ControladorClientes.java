/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.clases.Clientes;
import modelo.servicios.ServicioClientes;

/**
 *
 * @author sergi
 */
public class ControladorClientes {
    private ServicioClientes servicioClientes;

    public ControladorClientes(ServicioClientes servicioClientes) {
        this.servicioClientes = servicioClientes;
    }
    
    public Clientes registrarBuscarCliente(String identificacion, String nombre, String telefono, String email, String direccion, String ciudad){
        return servicioClientes.registrarOObtenerCliente(identificacion, nombre, telefono, email, direccion, ciudad);
    }
}
