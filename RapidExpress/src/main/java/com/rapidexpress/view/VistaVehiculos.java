/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template 
 */
package com.rapidexpress.view;
import com.rapidexpress.controller.ControladorVehiculos;
import com.rapidexpress.controller.ControladorAuditoria;
import com.rapidexpress.controller.ControladorMantenimientos;
import com.rapidexpress.model.entity.Mantenimientos;
import com.rapidexpress.model.entity.EstadoMantenimiento;
import com.rapidexpress.model.entity.Vehiculos;
import com.rapidexpress.model.entity.EstadoVehiculo;
import com.rapidexpress.model.dao.DaoMantenimientos;
import com.rapidexpress.model.dao.DaoVehiculos;
import com.rapidexpress.service.ServicioMantenimientos;
import com.rapidexpress.service.ServicioVehiculos;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
/**
 * Vista para gestion de vehiculos
 * @author Sebastian
 */
public class VistaVehiculos {
    private ControladorVehiculos controladorVehiculos;
    private ControladorMantenimientos controladorMantenimientos;
    public VistaVehiculos() {
        // Necesitamos instanciar las dependencias del controlador
        DaoVehiculos daoVehiculos = new DaoVehiculos();
        ServicioVehiculos servicioVehiculos = new ServicioVehiculos(daoVehiculos);
        ControladorAuditoria controladorAuditoria = new ControladorAuditoria();
        this.controladorVehiculos = new ControladorVehiculos(servicioVehiculos, controladorAuditoria);
        DaoMantenimientos daoMantenimientos = new DaoMantenimientos();
        ServicioMantenimientos servicioMantenimientos = new ServicioMantenimientos(daoMantenimientos, servicioVehiculos);
        this.controladorMantenimientos = new ControladorMantenimientos(servicioMantenimientos, controladorAuditoria);
    }
    /**
     * Muestra el menu principal de vehiculos
     */
    public void mostrarMenuVehiculos() {
        String[] opciones = {
            "Registrar vehiculo",
            "Listar vehiculos",
            "Buscar vehiculo por placa",
            "Actualizar datos de vehiculo",
            "Actualizar estado de vehiculo",
            "Programar mantenimiento de vehiculo",
            "Actualizar estado de un mantenimiento",
            "Consultar historial de mantenimientos de un vehiculo"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTION DE VEHICULOS", opciones);
            switch (opcion) {
                case 1:
                    registrarVehiculo();
                    break;
                case 2:
                    listarVehiculos();
                    break;
                case 3:
                    buscarVehiculoPorPlaca();
                    break;
                case 4:
                    actualizarDatosVehiculo();
                    break;
                case 5:
                    actualizarEstadoVehiculo();
                    break;
                case 6:
                    programarMantenimiento();
                    break;
                case 7:
                    actualizarEstadoMantenimiento();
                    break;
                case 8:
                    consultarHistorialMantenimientos();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opcion no valida");
            }
        }
    }
    /**
     * Registra un nuevo vehiculo
     */
    private void registrarVehiculo() {
        System.out.println("\nREGISTRO DE VEHICULO");
        System.out.println("---------------------------------------");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa: ");
            String marca = UtilidadConsola.leerTexto("  Marca: ");
            String modelo = UtilidadConsola.leerTexto("  Modelo: ");
            int anioFabricacion = UtilidadConsola.leerEntero("  Ano de fabricacion: ");
            double capacidadMaxima = UtilidadConsola.leerDouble("  Capacidad maxima (kg): ");
            controladorVehiculos.registrarVehiculo(
                placa, marca, modelo, anioFabricacion, capacidadMaxima
            );
            UtilidadConsola.mostrarExito("Vehiculo registrado exitosamente con placa: " + placa);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar vehiculo: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Lista todos los vehiculos registrados
     */
    private void listarVehiculos() {
        System.out.println("\nLISTADO DE VEHICULOS");
        System.out.println("---------------------------------------");
        try {
            List<Vehiculos> vehiculos = controladorVehiculos.listarVehiculos();
            if (vehiculos.isEmpty()) {
                UtilidadConsola.mostrarInfo("No hay vehiculos registrados");
            } else {
                System.out.printf("%-12s %-15s %-15s %-6s %-10s %-15s%n",
                    "PLACA", "MARCA", "MODELO", "ANO", "CAPACIDAD", "ESTADO");
                System.out.println("");
                for (Vehiculos v : vehiculos) {
                    System.out.printf("%-12s %-15s %-15s %-6d %-10.2f %-15s%n",
                        v.getPlaca(),
                        v.getMarca(),
                        v.getModelo(),
                        v.getAnio_fabricacion(),
                        v.getCapacidad_maxima_kg(),
                        v.getEstado().toString()
                    );
                }
                UtilidadConsola.mostrarInfo("Total de vehiculos: " + vehiculos.size());
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar vehiculos: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Busca un vehiculo por su placa
     */
    private void buscarVehiculoPorPlaca() {
        System.out.println("\nBUSQUEDA DE VEHICULO POR PLACA");
        System.out.println("---------------------------------------");
        try {
            String placa = UtilidadConsola.leerTexto(" Ingrese la placa a buscar: ");
            Vehiculos vehiculo = controladorVehiculos.buscarVehiculoPorPlaca(placa);
            if (vehiculo != null) {
                System.out.println("\nVEHICULO ENCONTRADO:");
                System.out.println("  Placa: " + vehiculo.getPlaca());
                System.out.println("  Marca: " + vehiculo.getMarca());
                System.out.println("  Modelo: " + vehiculo.getModelo());
                System.out.println("  Ano: " + vehiculo.getAnio_fabricacion());
                System.out.println("  Capacidad Maxima: " + vehiculo.getCapacidad_maxima_kg() + " kg");
                System.out.println("  Estado: " + vehiculo.getEstado());
            } else {
                UtilidadConsola.mostrarError("No se encontro un vehiculo con placa: " + placa);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al buscar vehiculo: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza los datos de un vehiculo
     */
    private void actualizarDatosVehiculo() {
        System.out.println("\n ACTUALIZACION DE DATOS DE VEHICULO");
        System.out.println("---------------------------------------");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa del vehiculo a actualizar: ");
            String nuevaMarca = UtilidadConsola.leerTexto("  Nueva marca: ");
            String nuevoModelo = UtilidadConsola.leerTexto("  Nuevo modelo: ");
            int nuevoAnio = UtilidadConsola.leerEntero("  Nuevo ano de fabricacion: ");
            double nuevaCapacidad = UtilidadConsola.leerDouble("  Nueva capacidad maxima (kg): ");
            controladorVehiculos.actualizarDatosVehiculo(
                placa, nuevaMarca, nuevoModelo, nuevoAnio, nuevaCapacidad
            );
            UtilidadConsola.mostrarExito("Datos del vehiculo actualizados correctamente");
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar datos: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza el estado de un vehiculo
     */
    private void actualizarEstadoVehiculo() {
        System.out.println("\nACTUALIZACION DE ESTADO DE VEHICULO");
        System.out.println("---------------------------------------");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa del vehiculo: ");
            System.out.println("\n Estados disponibles:");
            System.out.println("  [1] DISPONIBLE");
            System.out.println("  [2] EN_RUTA");
            System.out.println("  [3] EN_MANTENIMIENTO");
            int opcionEstado = UtilidadConsola.leerEntero("  Seleccione el nuevo estado: ");
            EstadoVehiculo nuevoEstado;
            switch (opcionEstado) {
                case 1:
                    nuevoEstado = EstadoVehiculo.DISPONIBLE;
                    break;
                case 2:
                    nuevoEstado = EstadoVehiculo.EN_RUTA;
                    break;
                case 3:
                    nuevoEstado = EstadoVehiculo.EN_MANTENIMIENTO;
                    break;
                default:
                    UtilidadConsola.mostrarError("Estado no valido");
                    return;
            }
            controladorVehiculos.actualizarEstadoVehiculo(placa, nuevoEstado);
            UtilidadConsola.mostrarExito("Estado del vehiculo actualizado a: " + nuevoEstado);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar estado: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Programa un nuevo mantenimiento para un vehiculo. El vehiculo debe estar DISPONIBLE;
     * al programarse, su estado pasa a EN_MANTENIMIENTO.
     */
    private void programarMantenimiento() {
        System.out.println("\nPROGRAMAR MANTENIMIENTO");
        System.out.println("---------------------------------------");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa del vehiculo: ");
            String tipo = UtilidadConsola.leerTexto("  Tipo de mantenimiento (ej. PREVENTIVO, CORRECTIVO): ");
            String descripcion = UtilidadConsola.leerTexto("  Descripcion: ");
            String fechaProgramadaStr = UtilidadConsola.leerTexto("  Fecha programada (dd/MM/yyyy): ");
            LocalDate fechaProgramada = LocalDate.parse(fechaProgramadaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            controladorMantenimientos.programarMantenimiento(placa, tipo, descripcion, fechaProgramada);
            UtilidadConsola.mostrarExito("Mantenimiento programado para el vehiculo: " + placa);
        } catch (DateTimeParseException e) {
            UtilidadConsola.mostrarError("Formato de fecha invalido. Use dd/MM/yyyy");
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al programar mantenimiento: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza el estado de un mantenimiento existente. Al marcarlo COMPLETADO,
     * el vehiculo vuelve a estado DISPONIBLE.
     */
    private void actualizarEstadoMantenimiento() {
        System.out.println("\nACTUALIZAR ESTADO DE MANTENIMIENTO");
        System.out.println("---------------------------------------");
        try {
            int idMantenimiento = UtilidadConsola.leerEntero("  ID del mantenimiento: ");
            String placaVehiculo = UtilidadConsola.leerTexto("  Placa del vehiculo: ");
            System.out.println("\n Estados disponibles:");
            System.out.println("  [1] EN_PROCESO");
            System.out.println("  [2] COMPLETADO");
            System.out.println("  [3] CANCELADO");
            int opcionEstado = UtilidadConsola.leerEntero("  Seleccione el nuevo estado: ");
            EstadoMantenimiento nuevoEstado;
            switch (opcionEstado) {
                case 1:
                    nuevoEstado = EstadoMantenimiento.EN_PROCESO;
                    break;
                case 2:
                    nuevoEstado = EstadoMantenimiento.COMPLETADO;
                    break;
                case 3:
                    nuevoEstado = EstadoMantenimiento.CANCELADO;
                    break;
                default:
                    UtilidadConsola.mostrarError("Estado no valido");
                    return;
            }
            double costo = UtilidadConsola.leerDouble("  Costo (0 si no aplica): ");
            String observaciones = UtilidadConsola.leerTexto("  Observaciones: ");
            controladorMantenimientos.actualizarEstadoMantenimiento(idMantenimiento, nuevoEstado, costo, observaciones, placaVehiculo);
            UtilidadConsola.mostrarExito("Mantenimiento actualizado a estado: " + nuevoEstado);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar mantenimiento: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Consulta el historial de mantenimientos registrados para un vehiculo
     */
    private void consultarHistorialMantenimientos() {
        System.out.println("\nHISTORIAL DE MANTENIMIENTOS");
        System.out.println("---------------------------------------");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa del vehiculo: ");
            List<Mantenimientos> historial = controladorMantenimientos.consultarHistorialMantenimientosPorVehiculo(placa);
            if (historial == null || historial.isEmpty()) {
                UtilidadConsola.mostrarInfo("No hay mantenimientos registrados para el vehiculo: " + placa);
            } else {
                System.out.println("\n Total de mantenimientos: " + historial.size());
                System.out.println("---------------------------------------");
                for (Mantenimientos m : historial) {
                    System.out.println("\n  ID: " + m.getId());
                    System.out.println("  Tipo: " + m.getTipoMantenimiento());
                    System.out.println("  Descripcion: " + m.getDescripcion());
                    System.out.println("  Fecha programada: " + m.getFechaProgramada());
                    System.out.println("  Fecha realizacion: " + m.getFechaRealizacion());
                    System.out.println("  Costo: " + m.getCosto());
                    System.out.println("  Estado: " + m.getEstado());
                    System.out.println("  Observaciones: " + m.getObservaciones());
                    System.out.println("---------------------------------------");
                }
                UtilidadConsola.mostrarExito("Historial consultado exitosamente");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al consultar historial de mantenimientos: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}