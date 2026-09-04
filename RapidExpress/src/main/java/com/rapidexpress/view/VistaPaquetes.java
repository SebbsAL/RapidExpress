/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.view;
import com.rapidexpress.config.Fabrica;
import com.rapidexpress.controller.ControladorPaquetes;
import com.rapidexpress.model.entity.Paquetes;
import com.rapidexpress.model.entity.HistorialPaquetes;
import java.util.List;
/**
 * Vista para gestion de paquetes
 * @author Sebastian
 */
public class VistaPaquetes {
    private ControladorPaquetes controladorPaquetes;
    public VistaPaquetes() {
        // Fabrica ya conecto Dao -> Servicio -> Controlador; solo se pide el controlador.
        this.controladorPaquetes = Fabrica.crearControladorPaquetes();
    }
    /**
     * Muestra el menu principal de paquetes
     */
    public void mostrarMenuPaquetes() {
        String[] opciones = {
            "Registrar nuevo paquete",
            "Buscar paquete por tracking",
            "Consultar trazabilidad de paquete",
            "Listar paquetes en bodega"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTION DE PAQUETES", opciones);
            switch (opcion) {
                case 1:
                    registrarPaquete();
                    break;
                case 2:
                    buscarPaquetePorTracking();
                    break;
                case 3:
                    consultarTrazabilidad();
                    break;
                case 4:
                    listarPaquetesEnBodega();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opcion no valida");
            }
        }
    }
    /**
     * Registra un nuevo paquete en el sistema
     */
    private void registrarPaquete() {
        System.out.println("\nREGISTRO DE NUEVO PAQUETE");
        System.out.println("---------------------------------------");
        try {
            String descripcion = UtilidadConsola.leerTextoObligatorio("  Descripcion del contenido: ");
            double peso = UtilidadConsola.leerDouble("  Peso (kg): ");
            String dimensiones = UtilidadConsola.leerTextoObligatorio("  Dimensiones (alto x ancho x largo): ");
            String direccionOrigen = UtilidadConsola.leerTextoObligatorio("  Direccion de origen: ");
            String direccionDestino = UtilidadConsola.leerTextoObligatorio("  Direccion de destino: ");
            System.out.println("\n DATOS DEL REMITENTE:");
            String remitenteIdentificacion = UtilidadConsola.leerTextoObligatorio("    Numero de identificacion: ");
            String remitenteNombre = UtilidadConsola.leerTextoObligatorio("    Nombre completo: ");
            String remitenteTelefono = UtilidadConsola.leerTextoObligatorio("    Telefono: ");
            String remitenteEmail = UtilidadConsola.leerTexto("    Email: ");
            String remitenteDireccion = UtilidadConsola.leerTextoObligatorio("    Direccion: ");
            String remitenteCiudad = UtilidadConsola.leerTextoObligatorio("    Ciudad: ");
            System.out.println("\n DATOS DEL DESTINATARIO:");
            String destinatarioIdentificacion = UtilidadConsola.leerTextoObligatorio("    Numero de identificacion: ");
            String destinatarioNombre = UtilidadConsola.leerTextoObligatorio("    Nombre completo: ");
            String destinatarioTelefono = UtilidadConsola.leerTextoObligatorio("    Telefono: ");
            String destinatarioEmail = UtilidadConsola.leerTexto("    Email: ");
            String destinatarioDireccion = UtilidadConsola.leerTextoObligatorio("    Direccion: ");
            String destinatarioCiudad = UtilidadConsola.leerTextoObligatorio("    Ciudad: ");
            String codigoSeguimiento = controladorPaquetes.registrarPaquete(
                descripcion, peso, dimensiones, direccionOrigen, direccionDestino,
                remitenteIdentificacion, remitenteNombre, remitenteTelefono, remitenteEmail, remitenteDireccion, remitenteCiudad,
                destinatarioIdentificacion, destinatarioNombre, destinatarioTelefono, destinatarioEmail, destinatarioDireccion, destinatarioCiudad
            );
            if (codigoSeguimiento != null) {
                System.out.println("\nPAQUETE REGISTRADO EXITOSAMENTE:");
                System.out.println("  Codigo de seguimiento: " + codigoSeguimiento);
                UtilidadConsola.mostrarExito("Paquete registrado con codigo: " + codigoSeguimiento);
            } else {
                UtilidadConsola.mostrarError("No se pudo registrar el paquete");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar paquete: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Busca un paquete por su codigo de tracking
     */
    private void buscarPaquetePorTracking() {
        System.out.println("\nBUSQUEDA DE PAQUETE POR TRACKING");
        System.out.println("---------------------------------------");
        try {
            String codigoSeguimiento = UtilidadConsola.leerTextoObligatorio("  Codigo de seguimiento: ");
            Paquetes paquete = controladorPaquetes.buscarPaquetePorTracking(codigoSeguimiento);
            if (paquete != null) {
                System.out.println("\nPAQUETE ENCONTRADO:");
                System.out.println("  Codigo de seguimiento: " + paquete.getCodigoSeguimiento());
                System.out.println("  Descripcion: " + paquete.getDescripcionContenido());
                System.out.println("  Peso: " + paquete.getPeso() + " kg");
                System.out.println("  Dimensiones: " + paquete.getDimensiones());
                System.out.println("  Direccion origen: " + paquete.getDireccionOrigen());
                System.out.println("  Direccion destino: " + paquete.getDireccionDestino());
                System.out.println("  Estado: " + paquete.getEstado());
                System.out.println("\n REMITENTE:");
                System.out.println("    Nombre: " + paquete.getRemitente().getNombreCompleto());
                System.out.println("    Identificacion: " + paquete.getRemitente().getNumeroIdentificacion());
                System.out.println("    Telefono: " + paquete.getRemitente().getTelefono());
                System.out.println("    Email: " + paquete.getRemitente().getEmail());
                System.out.println("\n DESTINATARIO:");
                System.out.println("    Nombre: " + paquete.getDestinatario().getNombreCompleto());
                System.out.println("    Identificacion: " + paquete.getDestinatario().getNumeroIdentificacion());
                System.out.println("    Telefono: " + paquete.getDestinatario().getTelefono());
                System.out.println("    Email: " + paquete.getDestinatario().getEmail());
                UtilidadConsola.mostrarExito("Paquete encontrado exitosamente");
            } else {
                UtilidadConsola.mostrarError("No se encontro un paquete con el codigo: " + codigoSeguimiento);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al buscar paquete: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Consulta la trazabilidad completa de un paquete
     */
    private void consultarTrazabilidad() {
        System.out.println("\nCONSULTAR TRAZABILIDAD DE PAQUETE");
        System.out.println("---------------------------------------");
        try {
            String codigoSeguimiento = UtilidadConsola.leerTextoObligatorio("  Codigo de seguimiento: ");
            List<HistorialPaquetes> historial = controladorPaquetes.consultarTrazabilidadPaquete(codigoSeguimiento);
            if (historial != null && !historial.isEmpty()) {
                System.out.println("\nTRAZABILIDAD DEL PAQUETE: " + codigoSeguimiento);
                System.out.println("---------------------------------------");
                for (HistorialPaquetes evento : historial) {
                    System.out.println("\n  Fecha: " + evento.getFechaRegistro());
                    System.out.println("   Evento: " + evento.getDescripcionEvento());
                    System.out.println("   Estado: " + evento.getEstado());
                    System.out.println("   Ubicacion: " + evento.getUbicacion());
                    System.out.println("---------------------------------------");
                }
                UtilidadConsola.mostrarExito("Trazabilidad consultada exitosamente");
            } else {
                UtilidadConsola.mostrarError("No hay historial registrado para el paquete: " + codigoSeguimiento);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al consultar trazabilidad: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Lista todos los paquetes que estan actualmente en bodega
     */
    private void listarPaquetesEnBodega() {
        System.out.println("\nPAQUETES EN BODEGA");
        System.out.println("---------------------------------------");
        try {
            List<Paquetes> paquetes = controladorPaquetes.listarPaquetesEnBodega();
            if (paquetes != null && !paquetes.isEmpty()) {
                System.out.println("\n Total de paquetes en bodega: " + paquetes.size());
                System.out.println("---------------------------------------");
                for (Paquetes paquete : paquetes) {
                    System.out.println("\n  Codigo: " + paquete.getCodigoSeguimiento());
                    System.out.println("   Descripcion: " + paquete.getDescripcionContenido());
                    System.out.println("    Peso: " + paquete.getPeso() + " kg");
                    System.out.println("   Dimensiones: " + paquete.getDimensiones());
                    System.out.println("   Origen: " + paquete.getDireccionOrigen());
                    System.out.println("   Destino: " + paquete.getDireccionDestino());
                    System.out.println("   Remitente: " + paquete.getRemitente().getNombreCompleto());
                    System.out.println("   Destinatario: " + paquete.getDestinatario().getNombreCompleto());
                    System.out.println("---------------------------------------");
                }
                UtilidadConsola.mostrarExito("Listado completado: " + paquetes.size() + " paquetes");
            } else {
                UtilidadConsola.mostrarInfo("No hay paquetes en bodega actualmente");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar paquetes: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}