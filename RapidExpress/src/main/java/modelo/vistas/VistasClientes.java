/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;
import controlador.ControladorClientes;
import controlador.ControladorAuditoria;
import modelo.clases.Clientes;
import modelo.persistencia.DaoClientes;
import modelo.servicios.ServicioClientes;
/**
 * Vista para gestión de clientes
 * @author Sebastian
 */
public class VistaClientes {
    private ControladorClientes controladorClientes;
    public VistaClientes() {
        DaoClientes daoClientes = new DaoClientes();
        ServicioClientes servicioClientes = new ServicioClientes(daoClientes);
        ControladorAuditoria controladorAuditoria = new ControladorAuditoria();
        this.controladorClientes = new ControladorClientes(servicioClientes, controladorAuditoria);
    }
    /**
     * Muestra el menú principal de clientes
     */
    public void mostrarMenuClientes() {
        String[] opciones = {
            "Registrar/Buscar cliente"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTIÓN DE CLIENTES", opciones);
            switch (opcion) {
                case 1:
                    registrarOBuscarCliente();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opción no válida");
            }
        }
    }
    /**
     * Registra o busca un cliente por identificación
     */
    private void registrarOBuscarCliente() {
        System.out.println("\n📝 REGISTRO/BÚSQUEDA DE CLIENTE");
        System.out.println("═══════════════════════════════════════");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Número de identificación: ");
            String nombreCompleto = UtilidadConsola.leerTexto("  Nombre completo: ");
            String telefono = UtilidadConsola.leerTexto("  Teléfono: ");
            String email = UtilidadConsola.leerTexto("  Email: ");
            String direccion = UtilidadConsola.leerTexto("  Dirección: ");
            String ciudad = UtilidadConsola.leerTexto("  Ciudad: ");
            Cliente cliente = controladorClientes.registrarBuscarCliente(
                identificacion, nombreCompleto, telefono, email, direccion, ciudad
            );
            if (cliente != null) {
                System.out.println("\n✅ CLIENTE PROCESADO:");
                System.out.println("  Identificación: " + cliente.getNumeroIdentificacion());
                System.out.println("  Nombre: " + cliente.getNombreCompleto());
                System.out.println("  Teléfono: " + cliente.getTelefono());
                System.out.println("  Email: " + cliente.getEmail());
                System.out.println("  Dirección: " + cliente.getDireccion());
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