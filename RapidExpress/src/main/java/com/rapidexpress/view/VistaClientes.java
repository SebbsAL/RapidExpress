/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.view;
import com.rapidexpress.config.Fabrica;
import com.rapidexpress.controller.ControladorClientes;
import com.rapidexpress.model.entity.Clientes;
import java.util.List;
/**
 * Vista para gestion de clientes
 * @author Sebastian
 */
public class VistaClientes {
    private ControladorClientes controladorClientes;
    public VistaClientes() {
        // Fabrica ya conecto Dao -> Servicio -> Controlador; solo se pide el controlador.
        this.controladorClientes = Fabrica.crearControladorClientes();
    }
    /**
     * Muestra el menu principal de clientes
     */
    public void mostrarMenuClientes() {
        String[] opciones = {
            "Registrar un nuevo cliente",
            "Listar clientes",
            "Buscar cliente por identificacion",
            "Actualizar datos de cliente"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTION DE CLIENTES", opciones);
            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    buscarClientePorIdentificacion();
                    break;
                case 4:
                    actualizarDatosCliente();
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
            String identificacion = UtilidadConsola.leerTextoObligatorio("  Numero de identificacion: ");
            String nombreCompleto = UtilidadConsola.leerTextoObligatorio("  Nombre completo: ");
            String telefono = UtilidadConsola.leerTextoObligatorio("  Telefono: ");
            String email = UtilidadConsola.leerTexto("  Email: ");
            String direccion = UtilidadConsola.leerTextoObligatorio("  Direccion: ");
            String ciudad = UtilidadConsola.leerTextoObligatorio("  Ciudad: ");
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
    /**
     * Lista todos los clientes registrados
     */
    private void listarClientes() {
        System.out.println("\nLISTADO DE CLIENTES");
        System.out.println("---------------------------------------");
        try {
            List<Clientes> clientes = controladorClientes.listarClientes();
            if (clientes.isEmpty()) {
                UtilidadConsola.mostrarInfo("No hay clientes registrados");
            } else {
                System.out.printf("%-15s %-25s %-12s %-25s %-20s %-15s%n",
                    "IDENTIFICACION", "NOMBRE", "TELEFONO", "EMAIL", "DIRECCION", "CIUDAD");
                System.out.println("");
                for (Clientes c : clientes) {
                    System.out.printf("%-15s %-25s %-12s %-25s %-20s %-15s%n",
                        c.getNumeroIdentificacion(),
                        c.getNombreCompleto(),
                        c.getTelefono(),
                        c.getEmail(),
                        c.getDireccion(),
                        c.getCiudad()
                    );
                }
                UtilidadConsola.mostrarInfo("Total de clientes: " + clientes.size());
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar clientes: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Busca un cliente por su numero de identificacion
     */
    private void buscarClientePorIdentificacion() {
        System.out.println("\nBUSQUEDA DE CLIENTE POR IDENTIFICACION");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTextoObligatorio("  Numero de identificacion: ");
            Clientes cliente = controladorClientes.buscarClientePorIdentificacion(identificacion);
            if (cliente != null) {
                System.out.println("\nCLIENTE ENCONTRADO:");
                System.out.println("  Identificacion: " + cliente.getNumeroIdentificacion());
                System.out.println("  Nombre: " + cliente.getNombreCompleto());
                System.out.println("  Telefono: " + cliente.getTelefono());
                System.out.println("  Email: " + cliente.getEmail());
                System.out.println("  Direccion: " + cliente.getDireccion());
                System.out.println("  Ciudad: " + cliente.getCiudad());
                System.out.println("  Fecha de registro: " + cliente.getFechaCreacion());
            } else {
                UtilidadConsola.mostrarError("No se encontro un cliente con identificacion: " + identificacion);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al buscar cliente: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza los datos de un cliente existente
     */
    private void actualizarDatosCliente() {
        System.out.println("\nACTUALIZACION DE DATOS DE CLIENTE");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTextoObligatorio("  Identificacion del cliente a actualizar: ");
            Clientes actual = controladorClientes.buscarClientePorIdentificacion(identificacion);
            if (actual == null) {
                UtilidadConsola.mostrarError("No se encontro un cliente con identificacion: " + identificacion);
                UtilidadConsola.pausar();
                return;
            }

            // Se muestran los datos actuales para que el usuario sepa que esta reemplazando.
            System.out.println("\n DATOS ACTUALES:");
            System.out.println("  Nombre: " + actual.getNombreCompleto());
            System.out.println("  Telefono: " + actual.getTelefono());
            System.out.println("  Email: " + actual.getEmail());
            System.out.println("  Direccion: " + actual.getDireccion());
            System.out.println("  Ciudad: " + actual.getCiudad());

            System.out.println("\n NUEVOS DATOS:");
            String nombreCompleto = UtilidadConsola.leerTextoObligatorio("  Nuevo nombre completo: ");
            String telefono = UtilidadConsola.leerTextoObligatorio("  Nuevo telefono: ");
            String email = UtilidadConsola.leerTexto("  Nuevo email: ");
            String direccion = UtilidadConsola.leerTextoObligatorio("  Nueva direccion: ");
            String ciudad = UtilidadConsola.leerTextoObligatorio("  Nueva ciudad: ");

            boolean exito = controladorClientes.actualizarDatosCliente(
                identificacion, nombreCompleto, telefono, email, direccion, ciudad
            );
            if (exito) {
                UtilidadConsola.mostrarExito("Datos del cliente actualizados correctamente");
            } else {
                UtilidadConsola.mostrarError("No se pudo actualizar el cliente " + identificacion);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar cliente: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}