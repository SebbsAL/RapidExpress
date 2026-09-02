/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import modelo.servicios.ServicioReportes;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Sebastian 
 */
public class ControladorReportes {
    private static final String USUARIO_SISTEMA = "SISTEMA";
    private ServicioReportes servicioReportes;
    private ControladorAuditoria controladorAuditoria;
    public ControladorReportes(ServicioReportes servicioReportes, ControladorAuditoria controladorAuditoria) {
        this.servicioReportes = servicioReportes;
        this.controladorAuditoria = controladorAuditoria;
    }
    public List<String> generarReporteEntregasPorConductor(String identificacionConductor, Date fechaInicio, Date fechaFin){
        List<String> reporte = servicioReportes.obtenerReporteEntregasPorConductor(identificacionConductor, fechaInicio, fechaFin);
        controladorAuditoria.registrar("REPORTES", "REPORTE_ENTREGAS",
            "Reporte de entregas del conductor " + identificacionConductor + " generado desde " + fechaInicio + " hasta " + fechaFin,
            USUARIO_SISTEMA);
        return reporte;
    }
    public List<String> generarHistorialRutasVehiculo(String placa){
        List<String> historial = servicioReportes.obtenerHistorialRutasVehiculo(placa);
        controladorAuditoria.registrar("REPORTES", "REPORTE_HISTORIAL_RUTAS",
            "Historial de rutas del vehículo " + placa + " generado",
            USUARIO_SISTEMA);
        return historial;
    }
}