
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.util.List;
import modelo.clases.Rutas;
import modelo.clases.RutaPaquetes;
import modelo.clases.EstadoEntrega;
import modelo.servicios.ServicioRutas;
/**
 *
 * @author Sebastian 
 */
public class ControladorRutas {
    private static final String USUARIO_SISTEMA = "SISTEMA";
    private final ServicioRutas servicioRutas;
    private final ControladorAuditoria controladorAuditoria;
    public ControladorRutas(ServicioRutas servicioRutas, ControladorAuditoria controladorAuditoria) {
        this.servicioRutas = servicioRutas;
        this.controladorAuditoria = controladorAuditoria;
    }
    /**
     * Crea una nueva hoja de ruta asignando vehículo, conductor y paquetes
     * @param placaVehiculo Placa del vehículo asignado
     * @param identificacionConductor Identificación del conductor asignado
     * @param codigosPaquetes Códigos de seguimiento de los paquetes a incluir en la ruta
     * @return Código de ruta generado, o null si hubo error
     */
    public String crearHojaDeRuta(String placaVehiculo, String identificacionConductor, List<String> codigosPaquetes) {
        String codigoRuta = servicioRutas.crearHojaDeRuta(placaVehiculo, identificacionConductor, codigosPaquetes);
        if (codigoRuta != null) {
            controladorAuditoria.registrar("RUTAS", "CREACION", "Hoja de ruta creada: " + codigoRuta + " con " + codigosPaquetes.size() + " paquetes", USUARIO_SISTEMA);
        }
        return codigoRuta;
    }
    /**
     * Inicia una ruta cambiando el estado de vehículo, conductor y paquetes
     * @param codigoRuta Código de la ruta a iniciar
     * @return true si se inició correctamente
     */
    public boolean iniciarRuta(String codigoRuta) {
        boolean exito = servicioRutas.iniciarRuta(codigoRuta);
        if (exito) {
            controladorAuditoria.registrar("RUTAS", "INICIO", "Ruta iniciada: " + codigoRuta, USUARIO_SISTEMA);
        }
        return exito;
    }
    /**
     * Registra la entrega de un paquete dentro de una ruta
     * @param codigoRuta Código de la ruta
     * @param codigoSeguimiento Código de seguimiento del paquete entregado
     * @param observaciones Observaciones sobre la entrega
     * @return true si se registró correctamente
     */
    public boolean registrarEntregaPaquete(String codigoRuta, String codigoSeguimiento, String observaciones, EstadoEntrega estadoEntrega) {
        boolean exito = servicioRutas.registrarEntregaPaquete(codigoRuta, codigoSeguimiento, observaciones, estadoEntrega);
        if (exito) {
            controladorAuditoria.registrar("RUTAS", "ENTREGA", "Entrega registrada para paquete " + codigoSeguimiento + " en ruta " + codigoRuta + " (" + estadoEntrega + ")", USUARIO_SISTEMA);
        }
        return exito;
    }
    /**
     * Finaliza una ruta liberando vehículo y conductor
     * @param codigoRuta Código de la ruta a finalizar
     * @return true si se finalizó correctamente
     */
    public boolean finalizarRuta(String codigoRuta) {
        boolean exito = servicioRutas.finalizarRuta(codigoRuta);
        if (exito) {
            controladorAuditoria.registrar("RUTAS", "FIN", "Ruta finalizada: " + codigoRuta, USUARIO_SISTEMA);
        }
        return exito;
    }
    /**
     * Lista todas las rutas activas (en proceso)
     * @return Lista de rutas con estado activo
     */
    public List<Rutas> listarRutasActivas() {
        return servicioRutas.listarRutasActivas();
    }
    /**
     * Obtiene el detalle de entrega de cada paquete asignado a una ruta
     * (orden de entrega, estado de entrega, fechas y observaciones)
     * @param codigoRuta Código de la ruta
     * @return Lista de detalle de entregas de la ruta
     */
    public List<RutaPaquetes> obtenerDetalleEntregas(String codigoRuta) {
        return servicioRutas.obtenerDetalleEntregas(codigoRuta);
    }
}
