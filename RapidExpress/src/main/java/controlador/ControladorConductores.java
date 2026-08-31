/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.List;
import modelo.clases.Conductores;
import modelo.servicios.ServicioConductores;

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
    
    public void actualizarDatosConductor(String identificacion, String nombre, String licencia, String telefono, String email){
        servicioConductores.actualizarDatosConductor(identificacion, nombre, licencia, telefono, email);
    }
    
    public List<Conductores> listarConductores(){
        return servicioConductores.listarConductores();
    }
    
    public Conductores buscarConductorPorIdentificacion(String identificacion){
        return servicioConductores.buscarConductorPorIdentificacion(identificacion);
    }
    
    public void registrarConductor(String identificacion, String nombre, String licencia, String telefono, String email){
        servicioConductores.registrarConductor(identificacion, nombre, licencia, telefono, email);
        controladorAuditoria.registrar("CONDUCTORES", "CREACION","Conductor registrado: "+identificacion,USUARIO_SISTEMA);
    }
    
    public void actualizarEstadoConductor(String identificacion, String nuevoEstado){
        servicioConductores.actualizarEstadoConductor(identificacion, nuevoEstado);
        controladorAuditoria.registrar("CONDUCTORES", "CAMBIO ESTADO","Conductor "+identificacion+"Cambio de estado "+nuevoEstado, USUARIO_SISTEMA);
    }
    
    public void asignarVehiculoAConductor(String identificacion, String placaVehiculo){
        servicioConductores.asignarVehiculoAConductor(identificacion, placaVehiculo);
        controladorAuditoria.registrar("CONDUCTORES", "ASIGNACION_VEHICULO", placaVehiculo+" asignada a conductor "+ identificacion, USUARIO_SISTEMA);
    }
}
