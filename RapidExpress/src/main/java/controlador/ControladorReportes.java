/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import modelo.servicios.ServicioReportes;
import java.util.Date;
/**
 *
 * @author sergi
 */
public class ControladorReportes {
    private static final String USUARIO_SISTEMA = "SISTEMA";
    private ServicioReportes servicioReportes;
    private ControladorAuditoria controladorAuditoria;
    public ControladorReportes(ServicioReportes servicioReportes, ControladorAuditoria controladorAuditoria) {
        this.servicioReportes = servicioReportes;
        this.controladorAuditoria = controladorAuditoria;
    }
    public void generarReporteEntregasPorConductor(Date fechaInicio, Date fechaFin){
        servicioReportes.obtenerReporteEntregasPorConductor(fechaInicio, fechaFin);
        controladorAuditoria.registrar("REPORTES", "REPORTE_ENTREGAS",
            "Reporte de entregas por conductor generado desde " + fechaInicio + " hasta " + fechaFin,
            USUARIO_SISTEMA);
    }
    public void generarHistorialRutasVehiculo(String placa){
        servicioReportes.obtenerHistorialRutasVehiculo(placa);
        controladorAuditoria.registrar("REPORTES", "REPORTE_HISTORIAL_RUTAS",
            "Historial de rutas del vehículo " + placa + " generado",
            USUARIO_SISTEMA);
    }
}