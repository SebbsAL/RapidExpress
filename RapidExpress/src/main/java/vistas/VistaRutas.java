/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;
import controlador.ControladorRutas;
import controlador.ControladorAuditoria;
import modelo.clases.Rutas;
import modelo.clases.Paquetes;
import modelo.clases.RutaPaquetes;
import modelo.persistencia.DaoConductores;
import modelo.persistencia.DaoPaquetes;
import modelo.persistencia.DaoRutas;
import modelo.persistencia.DaoVehiculos;
import modelo.servicios.ServicioAuditoria;
import modelo.servicios.ServicioConductores;
import modelo.servicios.ServicioRutas;
import modelo.servicios.ServicioVehiculos;
import java.util.List;
/**
 * Vista para gestión de rutas
 * @author Sebastian
 */
public class VistaRutas {
    private ControladorRutas controladorRutas;
    public VistaRutas() {
        DaoRutas daoRutas = new DaoRutas();
        DaoPaquetes daoPaquetes = new DaoPaquetes();
        ServicioVehiculos servicioVehiculos = new ServicioVehiculos(new DaoVehiculos());
        ServicioConductores servicioConductores = new ServicioConductores(new DaoConductores(), servicioVehiculos);
        ServicioAuditoria servicioAuditoria = new ServicioAuditoria();
        ServicioRutas servicioRutas = new ServicioRutas(daoRutas, servicioVehiculos, servicioConductores, daoPaquetes, servicioAuditoria);
        ControladorAuditoria controladorAuditoria = new ControladorAuditoria();
        this.controladorRutas = new ControladorRutas(servicioRutas, controladorAuditoria);
    }
    /**
     * Muestra el menú principal de rutas
     */
    public void mostrarMenuRutas() {
        String[] opciones = {
            "Crear hoja de ruta",
            "Iniciar ruta",
            "Registrar entrega de paquete",
            "Finalizar ruta",
            "Listar rutas activas",
            "Ver detalle de entregas de una ruta"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTIÓN DE RUTAS", opciones);
            switch (opcion) {
                case 1:
                    crearHojaDeRuta();
                    break;
                case 2:
                    iniciarRuta();
                    break;
                case 3:
                    registrarEntregaPaquete();
                    break;
                case 4:
                    finalizarRuta();
                    break;
                case 5:
                    listarRutasActivas();
                    break;
                case 6:
                    verDetalleEntregas();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opción no válida");
            }
        }
    }
    /**
     * Crea una nueva hoja de ruta asignando vehículo, conductor y paquetes
     */
    private void crearHojaDeRuta() {
        System.out.println("\n🚛 CREAR HOJA DE RUTA");
        System.out.println("═══════════════════════════════════════");
        try {
            String placaVehiculo = UtilidadConsola.leerTexto("  Placa del vehículo: ");
            String identificacionConductor = UtilidadConsola.leerTexto("  Identificación del conductor: ");
            System.out.println("\n  PAQUETES A ASIGNAR (ingrese códigos de seguimiento):");
            System.out.println("  (Ingrese un código vacío cuando termine)");
            List<String> codigosPaquetes = new java.util.ArrayList<>();
            int contador = 1;
            while (true) {
                String codigo = UtilidadConsola.leerTexto("    Paquete " + contador + " (o Enter para terminar): ");
                if (codigo == null || codigo.trim().isEmpty()) {
                    break;
                }
                codigosPaquetes.add(codigo.trim());
                contador++;
            }
            if (codigosPaquetes.isEmpty()) {
                UtilidadConsola.mostrarError("Debe asignar al menos un paquete a la ruta");
                return;
            }
            String codigoRuta = controladorRutas.crearHojaDeRuta(placaVehiculo, identificacionConductor, codigosPaquetes);
            if (codigoRuta != null) {
                System.out.println("\n✅ HOJA DE RUTA CREADA EXITOSAMENTE:");
                System.out.println("  Código de ruta: " + codigoRuta);
                System.out.println("  Vehículo: " + placaVehiculo);
                System.out.println("  Conductor: " + identificacionConductor);
                System.out.println("  Paquetes asignados: " + codigosPaquetes.size());
                UtilidadConsola.mostrarExito("Hoja de ruta creada con código: " + codigoRuta);
            } else {
                UtilidadConsola.mostrarError("No se pudo crear la hoja de ruta. Verifique que el vehículo y conductor estén disponibles.");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al crear hoja de ruta: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Inicia una ruta cambiando los estados de vehículo, conductor y paquetes
     */
    private void iniciarRuta() {
        System.out.println("\n▶️ INICIAR RUTA");
        System.out.println("═══════════════════════════════════════");
        try {
            String codigoRuta = UtilidadConsola.leerTexto("  Código de ruta: ");
            boolean exito = controladorRutas.iniciarRuta(codigoRuta);
            if (exito) {
                System.out.println("\n✅ RUTA INICIADA EXITOSAMENTE:");
                System.out.println("  Código de ruta: " + codigoRuta);
                System.out.println("  Estado: EN PROCESO");
                UtilidadConsola.mostrarExito("Ruta iniciada correctamente");
            } else {
                UtilidadConsola.mostrarError("No se pudo iniciar la ruta. Verifique el código y el estado actual de la ruta.");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al iniciar ruta: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Registra la entrega de un paquete específico dentro de una ruta
     */
    private void registrarEntregaPaquete() {
        System.out.println("\n📦 REGISTRAR ENTREGA DE PAQUETE");
        System.out.println("═══════════════════════════════════════");
        try {
            String codigoRuta = UtilidadConsola.leerTexto("  Código de ruta: ");
            String codigoSeguimiento = UtilidadConsola.leerTexto("  Código de seguimiento del paquete: ");
            String observaciones = UtilidadConsola.leerTexto("  Observaciones de la entrega (opcional): ");
            boolean exito = controladorRutas.registrarEntregaPaquete(codigoRuta, codigoSeguimiento, observaciones);
            if (exito) {
                System.out.println("\n✅ ENTREGA REGISTRADA EXITOSAMENTE:");
                System.out.println("  Ruta: " + codigoRuta);
                System.out.println("  Paquete: " + codigoSeguimiento);
                System.out.println("  Estado: ENTREGADO");
                UtilidadConsola.mostrarExito("Entrega registrada correctamente");
            } else {
                UtilidadConsola.mostrarError("No se pudo registrar la entrega. Verifique los códigos ingresados.");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar entrega: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Finaliza una ruta completada, liberando vehículo y conductor
     */
    private void finalizarRuta() {
        System.out.println("\n⏹️ FINALIZAR RUTA");
        System.out.println("═══════════════════════════════════════");
        try {
            String codigoRuta = UtilidadConsola.leerTexto("  Código de ruta: ");
            boolean exito = controladorRutas.finalizarRuta(codigoRuta);
            if (exito) {
                System.out.println("\n✅ RUTA FINALIZADA EXITOSAMENTE:");
                System.out.println("  Código de ruta: " + codigoRuta);
                System.out.println("  Estado: FINALIZADA");
                System.out.println("  Vehículo y conductor liberados para nuevas asignaciones");
                UtilidadConsola.mostrarExito("Ruta finalizada correctamente");
            } else {
                UtilidadConsola.mostrarError("No se pudo finalizar la ruta. Verifique el código y el estado actual de la ruta.");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al finalizar ruta: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Lista todas las rutas que están actualmente activas (en proceso)
     */
    private void listarRutasActivas() {
        System.out.println("\n🚛 RUTAS ACTIVAS");
        System.out.println("═══════════════════════════════════════");
        try {
            List<Rutas> rutas = controladorRutas.listarRutasActivas();
            if (rutas != null && !rutas.isEmpty()) {
                System.out.println("\n  Total de rutas activas: " + rutas.size());
                System.out.println("═══════════════════════════════════════");
                for (Rutas ruta : rutas) {
                    System.out.println("\n  🚛 Código de ruta: " + ruta.getCodigoRuta());
                    System.out.println("  📅 Fecha creación: " + ruta.getFechaCreacion());
                    System.out.println("  🚗 Vehículo: " + ruta.getVehiculo().getPlaca());
                    System.out.println("  👤 Conductor: " + ruta.getConductor().getNumeroIdentificacion() + " - " + ruta.getConductor().getNombreCompleto());
                    System.out.println("  📦 Paquetes: " + (ruta.getPaquetes() != null ? ruta.getPaquetes().size() : 0));
                    if (ruta.getPaquetes() != null) {
                        System.out.println("    Paquetes en esta ruta:");
                        for (Paquetes paquete : ruta.getPaquetes()) {
                            System.out.println("      • " + paquete.getCodigoSeguimiento() + " - " + paquete.getDescripcionContenido());
                        }
                    }
                    System.out.println("  ─────────────────────────────────────");
                }
                UtilidadConsola.mostrarExito("Listado completado: " + rutas.size() + " rutas activas");
            } else {
                UtilidadConsola.mostrarInfo("No hay rutas activas actualmente");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar rutas: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Muestra el detalle de entrega de cada paquete asignado a una ruta
     * (orden, estado de entrega, fechas y observaciones)
     */
    private void verDetalleEntregas() {
        System.out.println("\n📦 DETALLE DE ENTREGAS DE LA RUTA");
        System.out.println("═══════════════════════════════════════");
        try {
            String codigoRuta = UtilidadConsola.leerTexto("  Código de ruta: ");
            List<RutaPaquetes> detalle = controladorRutas.obtenerDetalleEntregas(codigoRuta);
            if (detalle == null || detalle.isEmpty()) {
                UtilidadConsola.mostrarInfo("No hay paquetes asignados a la ruta: " + codigoRuta);
            } else {
                System.out.println("\n  Total de paquetes en la ruta: " + detalle.size());
                System.out.println("═══════════════════════════════════════");
                for (RutaPaquetes rp : detalle) {
                    System.out.println("\n  📦 Orden: " + rp.getOrdenEntrega());
                    if (rp.getPaquete() != null) {
                        System.out.println("  Código: " + rp.getPaquete().getCodigoSeguimiento());
                        System.out.println("  Descripción: " + rp.getPaquete().getDescripcionContenido());
                    }
                    System.out.println("  Estado de entrega: " + rp.getEstadoEntrega());
                    System.out.println("  Fecha entrega estimada: " + rp.getFechaEntregaEstimada());
                    System.out.println("  Fecha entrega real: " + rp.getFechaEntregaReal());
                    System.out.println("  Observaciones: " + rp.getObservacionesEntrega());
                    System.out.println("  ─────────────────────────────────────");
                }
                UtilidadConsola.mostrarExito("Detalle de entregas consultado exitosamente");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al consultar detalle de entregas: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}