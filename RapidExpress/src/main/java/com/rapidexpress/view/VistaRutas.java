/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.view;
import com.rapidexpress.config.Fabrica;
import com.rapidexpress.controller.ControladorRutas;
import com.rapidexpress.model.entity.Rutas;
import com.rapidexpress.model.entity.Paquetes;
import com.rapidexpress.model.entity.RutaPaquetes;
import com.rapidexpress.model.entity.EstadoEntrega;
import java.util.List;
/**
 * Vista para gestion de rutas
 * @author Sebastian
 */
public class VistaRutas {
    private ControladorRutas controladorRutas;
    public VistaRutas() {
        // Fabrica ya conecto Dao -> Servicio -> Controlador; solo se pide el controlador.
        this.controladorRutas = Fabrica.crearControladorRutas();
    }
    /**
     * Muestra el menu principal de rutas
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
            int opcion = UtilidadConsola.mostrarMenu("GESTION DE RUTAS", opciones);
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
                    UtilidadConsola.mostrarError("Opcion no valida");
            }
        }
    }
    /**
     * Crea una nueva hoja de ruta asignando vehiculo, conductor y paquetes
     */
    private void crearHojaDeRuta() {
        System.out.println("\nCREAR HOJA DE RUTA");
        System.out.println("---------------------------------------");
        try {
            String placaVehiculo = UtilidadConsola.leerTexto("  Placa del vehiculo: ");
            String identificacionConductor = UtilidadConsola.leerTexto("  Identificacion del conductor: ");
            System.out.println("\n PAQUETES A ASIGNAR (ingrese codigos de seguimiento):");
            System.out.println("  (Ingrese un codigo vacio cuando termine)");
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
                System.out.println("\nHOJA DE RUTA CREADA EXITOSAMENTE:");
                System.out.println("  Codigo de ruta: " + codigoRuta);
                System.out.println("  Vehiculo: " + placaVehiculo);
                System.out.println("  Conductor: " + identificacionConductor);
                System.out.println("  Paquetes asignados: " + codigosPaquetes.size());
                UtilidadConsola.mostrarExito("Hoja de ruta creada con codigo: " + codigoRuta);
            } else {
                UtilidadConsola.mostrarError("No se pudo crear la hoja de ruta. Verifique que el vehiculo y conductor esten disponibles.");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al crear hoja de ruta: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Inicia una ruta cambiando los estados de vehiculo, conductor y paquetes
     */
    private void iniciarRuta() {
        System.out.println("\nINICIAR RUTA");
        System.out.println("---------------------------------------");
        try {
            String codigoRuta = UtilidadConsola.leerTexto("  Codigo de ruta: ");
            boolean exito = controladorRutas.iniciarRuta(codigoRuta);
            if (exito) {
                System.out.println("\nRUTA INICIADA EXITOSAMENTE:");
                System.out.println("  Codigo de ruta: " + codigoRuta);
                System.out.println("  Estado: EN PROCESO");
                UtilidadConsola.mostrarExito("Ruta iniciada correctamente");
            } else {
                UtilidadConsola.mostrarError("No se pudo iniciar la ruta. Verifique el codigo y el estado actual de la ruta.");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al iniciar ruta: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Registra la entrega de un paquete especifico dentro de una ruta
     */
    private void registrarEntregaPaquete() {
        System.out.println("\nREGISTRAR ENTREGA DE PAQUETE");
        System.out.println("---------------------------------------");
        try {
            String codigoRuta = UtilidadConsola.leerTexto("  Codigo de ruta: ");
            String codigoSeguimiento = UtilidadConsola.leerTexto("  Codigo de seguimiento del paquete: ");
            System.out.println("  Resultado de la entrega:");
            System.out.println("   [1] Entregado");
            System.out.println("   [2] Devuelto");
            System.out.println("   [3] Incidencia");
            int opcionResultado = UtilidadConsola.leerEntero("  Seleccione una opcion: ");
            EstadoEntrega estadoEntrega;
            switch (opcionResultado) {
                case 1:
                    estadoEntrega = EstadoEntrega.ENTREGADO;
                    break;
                case 2:
                    estadoEntrega = EstadoEntrega.DEVUELTO;
                    break;
                case 3:
                    estadoEntrega = EstadoEntrega.INCIDENCIA;
                    break;
                default:
                    UtilidadConsola.mostrarError("Opcion no valida");
                    return;
            }
            String observaciones = UtilidadConsola.leerTexto("  Observaciones de la entrega (opcional): ");
            boolean exito = controladorRutas.registrarEntregaPaquete(codigoRuta, codigoSeguimiento, observaciones, estadoEntrega);
            if (exito) {
                System.out.println("\nENTREGA REGISTRADA EXITOSAMENTE:");
                System.out.println("  Ruta: " + codigoRuta);
                System.out.println("  Paquete: " + codigoSeguimiento);
                System.out.println("  Estado: " + estadoEntrega);
                UtilidadConsola.mostrarExito("Entrega registrada correctamente");
            } else {
                UtilidadConsola.mostrarError("No se pudo registrar la entrega. Verifique los codigos ingresados.");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar entrega: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Finaliza una ruta completada, liberando vehiculo y conductor
     */
    private void finalizarRuta() {
        System.out.println("\nFINALIZAR RUTA");
        System.out.println("---------------------------------------");
        try {
            String codigoRuta = UtilidadConsola.leerTexto("  Codigo de ruta: ");
            boolean exito = controladorRutas.finalizarRuta(codigoRuta);
            if (exito) {
                System.out.println("\nRUTA FINALIZADA EXITOSAMENTE:");
                System.out.println("  Codigo de ruta: " + codigoRuta);
                System.out.println("  Estado: FINALIZADA");
                System.out.println("  Vehiculo y conductor liberados para nuevas asignaciones");
                UtilidadConsola.mostrarExito("Ruta finalizada correctamente");
            } else {
                UtilidadConsola.mostrarError("No se pudo finalizar la ruta. Verifique el codigo y el estado actual de la ruta.");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al finalizar ruta: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Lista todas las rutas que estan actualmente activas (en proceso)
     */
    private void listarRutasActivas() {
        System.out.println("\nRUTAS ACTIVAS");
        System.out.println("---------------------------------------");
        try {
            List<Rutas> rutas = controladorRutas.listarRutasActivas();
            if (rutas != null && !rutas.isEmpty()) {
                System.out.println("\n Total de rutas activas: " + rutas.size());
                System.out.println("---------------------------------------");
                for (Rutas ruta : rutas) {
                    System.out.println("\n  Codigo de ruta: " + ruta.getCodigoRuta());
                    System.out.println("   Fecha creacion: " + ruta.getFechaCreacion());
                    System.out.println("   Vehiculo: " + ruta.getVehiculo().getPlaca());
                    System.out.println("   Conductor: " + ruta.getConductor().getNumeroIdentificacion() + " - " + ruta.getConductor().getNombreCompleto());
                    System.out.println("   Paquetes: " + (ruta.getPaquetes() != null ? ruta.getPaquetes().size() : 0));
                    if (ruta.getPaquetes() != null) {
                        System.out.println("    Paquetes en esta ruta:");
                        for (Paquetes paquete : ruta.getPaquetes()) {
                            System.out.println("       " + paquete.getCodigoSeguimiento() + " - " + paquete.getDescripcionContenido());
                        }
                    }
                    System.out.println("---------------------------------------");
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
        System.out.println("\nDETALLE DE ENTREGAS DE LA RUTA");
        System.out.println("---------------------------------------");
        try {
            String codigoRuta = UtilidadConsola.leerTexto("  Codigo de ruta: ");
            List<RutaPaquetes> detalle = controladorRutas.obtenerDetalleEntregas(codigoRuta);
            if (detalle == null || detalle.isEmpty()) {
                UtilidadConsola.mostrarInfo("No hay paquetes asignados a la ruta: " + codigoRuta);
            } else {
                System.out.println("\n Total de paquetes en la ruta: " + detalle.size());
                System.out.println("---------------------------------------");
                for (RutaPaquetes rp : detalle) {
                    System.out.println("\n  Orden: " + rp.getOrdenEntrega());
                    if (rp.getPaquete() != null) {
                        System.out.println("  Codigo: " + rp.getPaquete().getCodigoSeguimiento());
                        System.out.println("  Descripcion: " + rp.getPaquete().getDescripcionContenido());
                    }
                    System.out.println("  Estado de entrega: " + rp.getEstadoEntrega());
                    System.out.println("  Fecha entrega estimada: " + rp.getFechaEntregaEstimada());
                    System.out.println("  Fecha entrega real: " + rp.getFechaEntregaReal());
                    System.out.println("  Observaciones: " + rp.getObservacionesEntrega());
                    System.out.println("---------------------------------------");
                }
                UtilidadConsola.mostrarExito("Detalle de entregas consultado exitosamente");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al consultar detalle de entregas: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}