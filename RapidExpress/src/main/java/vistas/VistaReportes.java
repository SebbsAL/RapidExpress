/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

import controlador.ControladorReportes;
import controlador.ControladorAuditoria;
import modelo.persistencia.DaoReportes;
import modelo.servicios.ServicioReportes;
import java.util.Date;
import java.util.List;
/**
 * Vista para generacion de reportes
 * @author Sebastian
 */
public class VistaReportes {
    private ControladorReportes controladorReportes;
    public VistaReportes() {
        DaoReportes daoReportes = new DaoReportes();
        ServicioReportes servicioReportes = new ServicioReportes(daoReportes);
        ControladorAuditoria controladorAuditoria = new ControladorAuditoria();
        this.controladorReportes = new ControladorReportes(servicioReportes, controladorAuditoria);
    }
    /**
     * Muestra el menu principal de reportes
     */
    public void mostrarMenuReportes() {
        String[] opciones = {
            "Generar reporte de entregas por conductor",
            "Generar historial de rutas por vehiculo"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GENERACION DE REPORTES", opciones);
            switch (opcion) {
                case 1:
                    generarReporteEntregasPorConductor();
                    break;
                case 2:
                    generarHistorialRutasVehiculo();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opcion no valida");
            }
        }
    }
    /**
     * Genera un reporte de entregas realizadas por un conductor en un rango de fechas
     */
    private void generarReporteEntregasPorConductor() {
        System.out.println("\nREPORTE DE ENTREGAS POR CONDUCTOR");
        System.out.println("---------------------------------------");
        try {
            String identificacionConductor = UtilidadConsola.leerTexto("  Identificacion del conductor: ");
            System.out.println("\n RANGO DE FECHAS:");
            System.out.println("  Formato: dd/MM/yyyy");
            String fechaInicioStr = UtilidadConsola.leerTexto("    Fecha inicio: ");
            String fechaFinStr = UtilidadConsola.leerTexto("    Fecha fin: ");
            // Convertir strings a Date (asumiendo formato dd/MM/yyyy)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date fechaInicio = sdf.parse(fechaInicioStr);
            Date fechaFin = sdf.parse(fechaFinStr);
            List<String> reporte = controladorReportes.generarReporteEntregasPorConductor(
                identificacionConductor, fechaInicio, fechaFin
            );
            if (reporte != null && !reporte.isEmpty()) {
                System.out.println("\n---------------------------------------");
                System.out.println("   REPORTE DE ENTREGAS");
                System.out.println("---------------------------------------");
                System.out.println("  Conductor: " + identificacionConductor);
                System.out.println("  Periodo: " + fechaInicioStr + " a " + fechaFinStr);
                System.out.println("---------------------------------------");
                for (String linea : reporte) {
                    System.out.println("  " + linea);
                }
                System.out.println("---------------------------------------");
                UtilidadConsola.mostrarExito("Reporte generado: " + reporte.size() + " registros");
            } else {
                UtilidadConsola.mostrarInfo("No se encontraron entregas para el conductor en el periodo especificado");
            }
        } catch (java.text.ParseException e) {
            UtilidadConsola.mostrarError("Formato de fecha invalido. Use dd/MM/yyyy");
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al generar reporte: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Genera el historial de rutas realizadas por un vehiculo especifico
     */
    private void generarHistorialRutasVehiculo() {
        System.out.println("\nHISTORIAL DE RUTAS POR VEHICULO");
        System.out.println("---------------------------------------");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa del vehiculo: ");
            List<String> reporte = controladorReportes.generarHistorialRutasVehiculo(placa);
            if (reporte != null && !reporte.isEmpty()) {
                System.out.println("\n---------------------------------------");
                System.out.println("   HISTORIAL DE RUTAS - VEHICULO: " + placa);
                System.out.println("---------------------------------------");
                for (String linea : reporte) {
                    System.out.println("  " + linea);
                }
                System.out.println("---------------------------------------");
                UtilidadConsola.mostrarExito("Historial generado: " + reporte.size() + " rutas");
            } else {
                UtilidadConsola.mostrarInfo("No se encontraron rutas para el vehiculo con placa: " + placa);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al generar historial: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}