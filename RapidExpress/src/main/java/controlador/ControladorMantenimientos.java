/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.time.LocalDate;
import java.util.List;
import modelo.clases.Mantenimientos;
import modelo.clases.EstadoMantenimiento;
import modelo.servicios.ServicioMantenimientos;

/**
 *
 * @author sergi 
 */
public class ControladorMantenimientos {
    private static final String USUARIO_SISTEMA = "SISTEMA";
    private ServicioMantenimientos serviciosMantenimientos;
    private ControladorAuditoria controladorAuditoria;

    public ControladorMantenimientos(ServicioMantenimientos serviciosMantenimientos, ControladorAuditoria controladorAuditoria) {
        this.serviciosMantenimientos = serviciosMantenimientos;
        this.controladorAuditoria = controladorAuditoria;
    }
    
    
    
    public boolean programarMantenimiento(String placaVehiculo, String tipo, String descripcion, LocalDate fechaProgramada){
        boolean exito = serviciosMantenimientos.programarMantenimiento(placaVehiculo, tipo, descripcion, fechaProgramada);
        if (exito) {
            controladorAuditoria.registrar("MANTENIMIENTO","PROGRAMAR_MANTENIMIENTO", "SE_PROGRAMO_UN_MANTENIMIENTO_TIPO: "+tipo+" PARA_EL_VEHICULO: "+placaVehiculo, USUARIO_SISTEMA);
        }
        return exito;
    }
    
    public boolean actualizarEstadoMantenimiento(int idMantenimiento, EstadoMantenimiento nuevoEstado, double costo, String observaciones, String placaVehiculo){
        boolean exito = serviciosMantenimientos.actualizarEstadoMantenimiento(idMantenimiento, nuevoEstado, costo, observaciones, placaVehiculo);
        if (exito) {
            controladorAuditoria.registrar("MANTENIMIENTO", "ACTUALIZAR_ESTADO_MANTENIMIENTO", "SE_ACTUALIZO_EL_ESTADO_DEL_MANTENIMIENTO_DEL_VEHICULO: "+placaVehiculo, USUARIO_SISTEMA);
        }
        return exito;
    }
    
    public List<Mantenimientos> consultarHistorialMantenimientosPorVehiculo(String placa){
        return serviciosMantenimientos.consultarHistorialMantenimientosPorVehiculo(placa);
    }
            
}
