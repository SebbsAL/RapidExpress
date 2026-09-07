/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.controller;

import java.util.List;
import com.rapidexpress.model.entity.Conductores;
import com.rapidexpress.model.entity.EstadoConductor;
import com.rapidexpress.service.ServicioConductores;

/**
 *
 * @author sergi 
 */
public class ControladorConductores {
    private static final String USUARIO_SISTEMA = "SISTEMA";
    private ServicioConductores servicioConductores;
    private ControladorAuditoria controladorAuditoria;

    public ControladorConductores(ServicioConductores servicioConductores, ControladorAuditoria controladorAuditoria) {
        this.servicioConductores = servicioConductores;
        this.controladorAuditoria = controladorAuditoria;
    }
    
    /**
     * Actualiza los datos personales y de licencia de un conductor.
     */
    public boolean actualizarDatosConductor(String identificacion, String nombre, String licencia, String telefono, String email){
        return servicioConductores.actualizarDatosConductor(identificacion, nombre, licencia, telefono, email);
    }

    /**
     * Lista todos los conductores registrados.
     */
    public List<Conductores> listarConductores(){
        return servicioConductores.listarConductores();
    }

    /**
     * Busca un conductor por su número de identificación.
     */
    public Conductores buscarConductorPorIdentificacion(String identificacion){
        return servicioConductores.buscarConductorPorIdentificacion(identificacion);
    }

    /**
     * Registra un nuevo conductor y deja constancia en la auditoría.
     */
    public void registrarConductor(String identificacion, String nombre, String licencia, String telefono, String email){
        servicioConductores.registrarConductor(identificacion, nombre, licencia, telefono, email);
        controladorAuditoria.registrar("CONDUCTORES", "CREACION","Conductor registrado: "+identificacion,USUARIO_SISTEMA);
    }

    /**
     * Actualiza el estado de un conductor y deja constancia en la auditoría.
     */
    public boolean actualizarEstadoConductor(String identificacion, EstadoConductor nuevoEstado){
        boolean exito = servicioConductores.actualizarEstadoConductor(identificacion, nuevoEstado);
        if (exito) {
            controladorAuditoria.registrar("CONDUCTORES", "CAMBIO ESTADO","Conductor "+identificacion+"Cambio de estado "+nuevoEstado, USUARIO_SISTEMA);
        }
        return exito;
    }

    /**
     * Asigna un vehículo a un conductor y deja constancia en la auditoría.
     */
    public boolean asignarVehiculoAConductor(String identificacion, String placaVehiculo){
        boolean exito = servicioConductores.asignarVehiculoAConductor(identificacion, placaVehiculo);
        if (exito) {
            controladorAuditoria.registrar("CONDUCTORES", "ASIGNACION_VEHICULO", placaVehiculo+" asignada a conductor "+ identificacion, USUARIO_SISTEMA);
        }
        return exito;
    }
    
    /**
     * Lista los conductores que actualmente no tienen ningun vehiculo asignado.
     */
    public List<Conductores> conductoresSinVehiculoAsignado(){
        return servicioConductores.conductoresSinVehiculoAsignado();
    }

    /**
     * Desasigna el vehículo activo de un conductor y deja constancia en la auditoría.
     */
    public boolean desasignarVehiculoDeConductor(String identificacion){
        boolean exito = servicioConductores.desasignarVehiculoDeConductor(identificacion);
        if (exito) {
            controladorAuditoria.registrar("CONDUCTORES", "DESASIGNACION_VEHICULO", "Conductor "+identificacion+" desasignado de su vehiculo", USUARIO_SISTEMA);
        }
        return exito;
    }
}
