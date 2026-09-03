/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.view;
import com.rapidexpress.config.Fabrica;
import com.rapidexpress.controller.ControladorConductores;
import com.rapidexpress.model.entity.Conductores;
import com.rapidexpress.model.entity.EstadoConductor;
import java.util.List;
/**
 * Vista para gestion de conductores
 * @author Sebastian
 */
public class VistaConductores {
    private ControladorConductores controladorConductores;
    public VistaConductores() {
        // Fabrica ya conecto Dao -> Servicio -> Controlador; solo se pide el controlador.
        this.controladorConductores = Fabrica.crearControladorConductores();
    }
    /**
     * Muestra el menu principal de conductores
     */
    public void mostrarMenuConductores() {
        String[] opciones = {
            "Registrar conductor",
            "Listar conductores",
            "Buscar conductor por identificacion",
            "Actualizar datos de conductor",
            "Actualizar estado de conductor",
            "Asignar vehiculo a conductor"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTION DE CONDUCTORES", opciones);
            switch (opcion) {
                case 1:
                    registrarConductor();
                    break;
                case 2:
                    listarConductores();
                    break;
                case 3:
                    buscarConductorPorIdentificacion();
                    break;
                case 4:
                    actualizarDatosConductor();
                    break;
                case 5:
                    actualizarEstadoConductor();
                    break;
                case 6:
                    asignarVehiculoAConductor();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opcion no valida");
            }
        }
    }
    /**
     * Registra un nuevo conductor
     */
    private void registrarConductor() {
        System.out.println("\nREGISTRO DE CONDUCTOR");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Numero de identificacion: ");
            String nombreCompleto = UtilidadConsola.leerTexto("  Nombre completo: ");
            String tipoLicencia = UtilidadConsola.leerTexto("  Tipo de licencia: ");
            String telefono = UtilidadConsola.leerTexto("  Telefono: ");
            String email = UtilidadConsola.leerTexto("  Email: ");
            controladorConductores.registrarConductor(
                identificacion, nombreCompleto, tipoLicencia, telefono, email
            );
            UtilidadConsola.mostrarExito("Conductor registrado exitosamente con identificacion: " + identificacion);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar conductor: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Lista todos los conductores registrados
     */
    private void listarConductores() {
        System.out.println("\nLISTADO DE CONDUCTORES");
        System.out.println("---------------------------------------");
        try {
            List<Conductores> conductores = controladorConductores.listarConductores();
            if (conductores.isEmpty()) {
                UtilidadConsola.mostrarInfo("No hay conductores registrados");
            } else {
                System.out.printf("%-15s %-25s %-10s %-12s %-20s %-15s%n",
                    "IDENTIFICACION", "NOMBRE", "LICENCIA", "TELEFONO", "EMAIL", "ESTADO");
                System.out.println("");
                for (Conductores c : conductores) {
                    System.out.printf("%-15s %-25s %-10s %-12s %-20s %-15s%n",
                        c.getNumeroIdentificacion(),
                        c.getNombreCompleto(),
                        c.getTipoLicencia(),
                        c.getTelefono(),
                        c.getEmail(),
                        c.getEstado().toString()
                    );
                }
                UtilidadConsola.mostrarInfo("Total de conductores: " + conductores.size());
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar conductores: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Busca un conductor por su identificacion
     */
    private void buscarConductorPorIdentificacion() {
        System.out.println("\nBUSQUEDA DE CONDUCTOR POR IDENTIFICACION");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTexto(" Ingrese la identificacion a buscar: ");
            Conductores conductor = controladorConductores.buscarConductorPorIdentificacion(identificacion);
            if (conductor != null) {
                System.out.println("\nCONDUCTOR ENCONTRADO:");
                System.out.println("  Identificacion: " + conductor.getNumeroIdentificacion());
                System.out.println("  Nombre: " + conductor.getNombreCompleto());
                System.out.println("  Tipo de Licencia: " + conductor.getTipoLicencia());
                System.out.println("  Telefono: " + conductor.getTelefono());
                System.out.println("  Email: " + conductor.getEmail());
                System.out.println("  Estado: " + conductor.getEstado());
            } else {
                UtilidadConsola.mostrarError("No se encontro un conductor con identificacion: " + identificacion);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al buscar conductor: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza los datos de un conductor
     */
    private void actualizarDatosConductor() {
        System.out.println("\n ACTUALIZACION DE DATOS DE CONDUCTOR");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Identificacion del conductor: ");
            String nombreCompleto = UtilidadConsola.leerTexto("  Nuevo nombre completo: ");
            String tipoLicencia = UtilidadConsola.leerTexto("  Nuevo tipo de licencia: ");
            String telefono = UtilidadConsola.leerTexto("  Nuevo telefono: ");
            String email = UtilidadConsola.leerTexto("  Nuevo email: ");
            controladorConductores.actualizarDatosConductor(
                identificacion, nombreCompleto, tipoLicencia, telefono, email
            );
            UtilidadConsola.mostrarExito("Datos del conductor actualizados correctamente");
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar datos: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza el estado de un conductor
     */
    private void actualizarEstadoConductor() {
        System.out.println("\nACTUALIZACION DE ESTADO DE CONDUCTOR");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Identificacion del conductor: ");
            System.out.println("\n Estados disponibles:");
            System.out.println("  [1] ACTIVO");
            System.out.println("  [2] DE_VACACIONES");
            System.out.println("  [3] INACTIVO");
            int opcionEstado = UtilidadConsola.leerEntero("  Seleccione el nuevo estado: ");
            EstadoConductor nuevoEstado;
            switch (opcionEstado) {
                case 1:
                    nuevoEstado = EstadoConductor.ACTIVO;
                    break;
                case 2:
                    nuevoEstado = EstadoConductor.DE_VACACIONES;
                    break;
                case 3:
                    nuevoEstado = EstadoConductor.INACTIVO;
                    break;
                default:
                    UtilidadConsola.mostrarError("Estado no valido");
                    return;
            }
            controladorConductores.actualizarEstadoConductor(identificacion, nuevoEstado);
            UtilidadConsola.mostrarExito("Estado del conductor actualizado a: " + nuevoEstado);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar estado: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Asigna un vehiculo a un conductor
     */
    private void asignarVehiculoAConductor() {
        System.out.println("\nASIGNAR VEHICULO A CONDUCTOR");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Identificacion del conductor: ");
            String placaVehiculo = UtilidadConsola.leerTexto("  Placa del vehiculo: ");
            controladorConductores.asignarVehiculoAConductor(identificacion, placaVehiculo);
            UtilidadConsola.mostrarExito("Vehiculo " + placaVehiculo + " asignado al conductor " + identificacion);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al asignar vehiculo: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}