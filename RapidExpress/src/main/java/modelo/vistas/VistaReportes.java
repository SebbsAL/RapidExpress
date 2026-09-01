/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vistas;

import controlador.ControladorReportes;
import controlador.ControladorAuditoria;
import modelo.persistencia.DaoReportes;
import modelo.servicios.ServicioReportes;
import java.util.Date;
import java.util.List;
import vistas.UtilidadConsola;
/**
 * Vista para generación de reportes
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
     * Muestra el menú principal de reportes
     */
    public void mostrarMenuReportes() {
        String[] opciones = {
            "Generar reporte de entregas por conductor",
            "Generar historial de rutas por vehículo"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GENERACIÓN DE REPORTES", opciones);
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
                    UtilidadConsola.mostrarError("Opción no válida");
            }
        }
    }
    /**
     * Genera un reporte de entregas realizadas por un conductor en un rango de fechas
     */
    private void generarReporteEntregasPorConductor() {
        System.out.println("\n📊 REPORTE DE ENTREGAS POR CONDUCTOR");
        System.out.println("═══════════════════════════════════════");
        try {
            String identificacionConductor = UtilidadConsola.leerTexto("  Identificación del conductor: ");
            System.out.println("\n  RANGO DE FECHAS:");
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
                System.out.println("\n═══════════════════════════════════════");
                System.out.println("  📋 REPORTE DE ENTREGAS");
                System.out.println("═══════════════════════════════════════");
                System.out.println("  Conductor: " + identificacionConductor);
                System.out.println("  Período: " + fechaInicioStr + " a " + fechaFinStr);
                System.out.println("═══════════════════════════════════════");
                for (String linea : reporte) {
                    System.out.println("  " + linea);
                }
                System.out.println("═══════════════════════════════════════");
                UtilidadConsola.mostrarExito("Reporte generado: " + reporte.size() + " registros");
            } else {
                UtilidadConsola.mostrarInfo("No se encontraron entregas para el conductor en el período especificado");
            }
        } catch (java.text.ParseException e) {
            UtilidadConsola.mostrarError("Formato de fecha inválido. Use dd/MM/yyyy");
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al generar reporte: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Genera el historial de rutas realizadas por un vehículo específico
     */
    private void generarHistorialRutasVehiculo() {
        System.out.println("\n📋 HISTORIAL DE RUTAS POR VEHÍCULO");
        System.out.println("═══════════════════════════════════════");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa del vehículo: ");
            List<String> reporte = controladorReportes.generarHistorialRutasVehiculo(placa);
            if (reporte != null && !reporte.isEmpty()) {
                System.out.println("\n═══════════════════════════════════════");
                System.out.println("  🚗 HISTORIAL DE RUTAS - VEHÍCULO: " + placa);
                System.out.println("═══════════════════════════════════════");
                for (String linea : reporte) {
                    System.out.println("  " + linea);
                }
                System.out.println("═══════════════════════════════════════");
                UtilidadConsola.mostrarExito("Historial generado: " + reporte.size() + " rutas");
            } else {
                UtilidadConsola.mostrarInfo("No se encontraron rutas para el vehículo con placa: " + placa);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al generar historial: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}