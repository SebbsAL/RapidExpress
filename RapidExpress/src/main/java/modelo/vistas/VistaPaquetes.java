/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;
import controlador.ControladorPaquetes;
import controlador.ControladorAuditoria;
import modelo.clases.Paquetes;
import modelo.clases.HistorialPaquetes;
import modelo.persistencia.DaoPaquetes;
import modelo.servicios.ServicioPaquetes;
import java.util.List;
/**
 * Vista para gestión de paquetes
 * @author Sebastian
 */
public class VistaPaquetes {
    private ControladorPaquetes controladorPaquetes;
    public VistaPaquetes() {
        DaoPaquetes daoPaquetes = new DaoPaquetes();
        ServicioPaquetes servicioPaquetes = new ServicioPaquetes(daoPaquetes);
        ControladorAuditoria controladorAuditoria = new ControladorAuditoria();
        this.controladorPaquetes = new ControladorPaquetes(servicioPaquetes, controladorAuditoria);
    }
    /**
     * Muestra el menú principal de paquetes
     */
    public void mostrarMenuPaquetes() {
        String[] opciones = {
            "Registrar nuevo paquete",
            "Buscar paquete por tracking",
            "Consultar trazabilidad de paquete",
            "Listar paquetes en bodega"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTIÓN DE PAQUETES", opciones);
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
                    UtilidadConsola.mostrarError("Opción no válida");
            }
        }
    }
    /**
     * Registra un nuevo paquete en el sistema
     */
    private void registrarPaquete() {
        System.out.println("\n📦 REGISTRO DE NUEVO PAQUETE");
        System.out.println("═══════════════════════════════════════");
        try {
            String descripcion = UtilidadConsola.leerTexto("  Descripción del contenido: ");
            double peso = UtilidadConsola.leerDouble("  Peso (kg): ");
            String dimensiones = UtilidadConsola.leerTexto("  Dimensiones (alto x ancho x largo): ");
            String direccionOrigen = UtilidadConsola.leerTexto("  Dirección de origen: ");
            String direccionDestino = UtilidadConsola.leerTexto("  Dirección de destino: ");
            System.out.println("\n  DATOS DEL REMITENTE:");
            String remitenteIdentificacion = UtilidadConsola.leerTexto("    Número de identificación: ");
            String remitenteNombre = UtilidadConsola.leerTexto("    Nombre completo: ");
            String remitenteTelefono = UtilidadConsola.leerTexto("    Teléfono: ");
            String remitenteEmail = UtilidadConsola.leerTexto("    Email: ");
            String remitenteDireccion = UtilidadConsola.leerTexto("    Dirección: ");
            String remitenteCiudad = UtilidadConsola.leerTexto("    Ciudad: ");
            System.out.println("\n  DATOS DEL DESTINATARIO:");
            String destinatarioIdentificacion = UtilidadConsola.leerTexto("    Número de identificación: ");
            String destinatarioNombre = UtilidadConsola.leerTexto("    Nombre completo: ");
            String destinatarioTelefono = UtilidadConsola.leerTexto("    Teléfono: ");
            String destinatarioEmail = UtilidadConsola.leerTexto("    Email: ");
            String destinatarioDireccion = UtilidadConsola.leerTexto("    Dirección: ");
            String destinatarioCiudad = UtilidadConsola.leerTexto("    Ciudad: ");
            String codigoSeguimiento = controladorPaquetes.registrarPaquete(
                descripcion, peso, dimensiones, direccionOrigen, direccionDestino,
                remitenteIdentificacion, remitenteNombre, remitenteTelefono, remitenteEmail, remitenteDireccion, remitenteCiudad,
                destinatarioIdentificacion, destinatarioNombre, destinatarioTelefono, destinatarioEmail, destinatarioDireccion, destinatarioCiudad
            );
            if (codigoSeguimiento != null) {
                System.out.println("\n✅ PAQUETE REGISTRADO EXITOSAMENTE:");
                System.out.println("  Código de seguimiento: " + codigoSeguimiento);
                UtilidadConsola.mostrarExito("Paquete registrado con código: " + codigoSeguimiento);
            } else {
                UtilidadConsola.mostrarError("No se pudo registrar el paquete");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar paquete: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Busca un paquete por su código de tracking
     */
    private void buscarPaquetePorTracking() {
        System.out.println("\n🔍 BÚSQUEDA DE PAQUETE POR TRACKING");
        System.out.println("═══════════════════════════════════════");
        try {
            String codigoSeguimiento = UtilidadConsola.leerTexto("  Código de seguimiento: ");
            Paquetes paquete = controladorPaquetes.buscarPaquetePorTracking(codigoSeguimiento);
            if (paquete != null) {
                System.out.println("\n✅ PAQUETE ENCONTRADO:");
                System.out.println("  Código de seguimiento: " + paquete.getCodigoSeguimiento());
                System.out.println("  Descripción: " + paquete.getDescripcionContenido());
                System.out.println("  Peso: " + paquete.getPeso() + " kg");
                System.out.println("  Dimensiones: " + paquete.getDimensiones());
                System.out.println("  Dirección origen: " + paquete.getDireccionOrigen());
                System.out.println("  Dirección destino: " + paquete.getDireccionDestino());
                System.out.println("  Estado: " + paquete.getEstado());
                System.out.println("\n  REMITENTE:");
                System.out.println("    Nombre: " + paquete.getRemitente().getNombreCompleto());
                System.out.println("    Identificación: " + paquete.getRemitente().getNumeroIdentificacion());
                System.out.println("    Teléfono: " + paquete.getRemitente().getTelefono());
                System.out.println("    Email: " + paquete.getRemitente().getEmail());
                System.out.println("\n  DESTINATARIO:");
                System.out.println("    Nombre: " + paquete.getDestinatario().getNombreCompleto());
                System.out.println("    Identificación: " + paquete.getDestinatario().getNumeroIdentificacion());
                System.out.println("    Teléfono: " + paquete.getDestinatario().getTelefono());
                System.out.println("    Email: " + paquete.getDestinatario().getEmail());
                UtilidadConsola.mostrarExito("Paquete encontrado exitosamente");
            } else {
                UtilidadConsola.mostrarError("No se encontró un paquete con el código: " + codigoSeguimiento);
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
        System.out.println("\n📋 CONSULTAR TRAZABILIDAD DE PAQUETE");
        System.out.println("═══════════════════════════════════════");
        try {
            String codigoSeguimiento = UtilidadConsola.leerTexto("  Código de seguimiento: ");
            List<HistorialPaquetes> historial = controladorPaquetes.consultarTrazabilidadPaquete(codigoSeguimiento);
            if (historial != null && !historial.isEmpty()) {
                System.out.println("\n📦 TRAZABILIDAD DEL PAQUETE: " + codigoSeguimiento);
                System.out.println("═══════════════════════════════════════");
                for (HistorialPaquetes evento : historial) {
                    System.out.println("\n  📅 Fecha: " + evento.getFechaRegistro());
                    System.out.println("  📝 Evento: " + evento.getDescripcionEvento());
                    System.out.println("  📄 Estado: " + evento.getEstado());
                    System.out.println("  📍 Ubicación: " + evento.getUbicacion());
                    System.out.println("  ─────────────────────────────────────");
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
     * Lista todos los paquetes que están actualmente en bodega
     */
    private void listarPaquetesEnBodega() {
        System.out.println("\n📦 PAQUETES EN BODEGA");
        System.out.println("═══════════════════════════════════════");
        try {
            List<Paquetes> paquetes = controladorPaquetes.listarPaquetesEnBodega();
            if (paquetes != null && !paquetes.isEmpty()) {
                System.out.println("\n  Total de paquetes en bodega: " + paquetes.size());
                System.out.println("═══════════════════════════════════════");
                for (Paquetes paquete : paquetes) {
                    System.out.println("\n  📦 Código: " + paquete.getCodigoSeguimiento());
                    System.out.println("  📝 Descripción: " + paquete.getDescripcionContenido());
                    System.out.println("  ⚖️  Peso: " + paquete.getPeso() + " kg");
                    System.out.println("  📏 Dimensiones: " + paquete.getDimensiones());
                    System.out.println("  📍 Origen: " + paquete.getDireccionOrigen());
                    System.out.println("  🎯 Destino: " + paquete.getDireccionDestino());
                    System.out.println("  👤 Remitente: " + paquete.getRemitente().getNombreCompleto());
                    System.out.println("  👤 Destinatario: " + paquete.getDestinatario().getNombreCompleto());
                    System.out.println("  ─────────────────────────────────────");
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