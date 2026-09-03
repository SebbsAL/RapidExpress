/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.view;
import com.rapidexpress.controller.ControladorClientes;
import com.rapidexpress.model.entity.Clientes;
import com.rapidexpress.model.dao.DaoClientes;
import com.rapidexpress.service.ServicioClientes;
/**
 * Vista para gestion de clientes 
 * @author Sebastian
 */
public class VistaClientes {
    private ControladorClientes controladorClientes;
    public VistaClientes() {
        DaoClientes daoClientes = new DaoClientes();
        ServicioClientes servicioClientes = new ServicioClientes(daoClientes);
        this.controladorClientes = new ControladorClientes(servicioClientes);
    }
    /**
     * Muestra el menu principal de clientes
     */
    public void mostrarMenuClientes() {
        String[] opciones = {
            "Registrar un nuevo cliente"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTION DE CLIENTES", opciones);
            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opcion no valida");
            }
        }
    }
    /**
     * Registra o busca un cliente por identificacion
     */
    private void registrarCliente() {
        System.out.println("\nREGISTRO/BUSQUEDA DE CLIENTE");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Numero de identificacion: ");
            String nombreCompleto = UtilidadConsola.leerTexto("  Nombre completo: ");
            String telefono = UtilidadConsola.leerTexto("  Telefono: ");
            String email = UtilidadConsola.leerTexto("  Email: ");
            String direccion = UtilidadConsola.leerTexto("  Direccion: ");
            String ciudad = UtilidadConsola.leerTexto("  Ciudad: ");
            Clientes cliente = controladorClientes.registrarBuscarCliente(
                identificacion, nombreCompleto, telefono, email, direccion, ciudad
            );
            if (cliente != null) {
                System.out.println("\nCLIENTE PROCESADO:");
                System.out.println("  Identificacion: " + cliente.getNumeroIdentificacion());
                System.out.println("  Nombre: " + cliente.getNombreCompleto());
                System.out.println("  Telefono: " + cliente.getTelefono());
                System.out.println("  Email: " + cliente.getEmail());
                System.out.println("  Direccion: " + cliente.getDireccion());
                System.out.println("  Ciudad: " + cliente.getCiudad());
                UtilidadConsola.mostrarExito("Cliente procesado exitosamente");
            } else {
                UtilidadConsola.mostrarError("No se pudo procesar el cliente");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al procesar cliente: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}