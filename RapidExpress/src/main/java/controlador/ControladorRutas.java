/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.util.List;
import modelo.clases.Paquetes;
import modelo.clases.Rutas;
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
     * Crea una nueva hoja de ruta asignando vehículos, conductor y paquetes
     * @param placaVehiculo Placa del vehículo asignado
     * @param identificacionConductor Identificación del conductor asignado
     * @param paquetesSeleccionados Lista de paquetes a incluir en la ruta
     * @return Código de ruta generado, o null si hubo error
     */
    public String crearHojaDeRuta(String placaVehiculo, String identificacionConductor, List<Paquetes> paquetesSeleccionados) {
        String codigoRuta = servicioRutas.crearHojaDeRuta(placaVehiculo, identificacionConductor, paquetesSeleccionados);
        if (codigoRuta != null) {
            controladorAuditoria.registrar("RUTAS", "CREACION", "Hoja de ruta creada: " + codigoRuta + " con " + paquetesSeleccionados.size() + " paquetes", USUARIO_SISTEMA);
        }
        return codigoRuta;
    }
    /**
     * Inicia una ruta cambiando el estado de vehículos, conductor y paquetes
     * @param codigoRuta Código de la ruta a iniciar
     * @param paquetesDeRuta Lista de paquetes incluidos en la ruta
     * @param placaVehiculo Placa del vehículo asignado
     * @param idConductor Identificación del conductor asignado
     */
    public void iniciarRuta(String codigoRuta, List<Paquetes> paquetesDeRuta, String placaVehiculo, String idConductor) {
        servicioRutas.iniciarRuta(codigoRuta, paquetesDeRuta, placaVehiculo, idConductor);
        controladorAuditoria.registrar("RUTAS", "INICIO", "Ruta iniciada: " + codigoRuta, USUARIO_SISTEMA);
    }
    /**
     * Registra la entrega de un paquete dentro de una ruta
     * @param codigoRuta Código de la ruta
     * @param paquete Paquete entregado
     * @param estadoEntrega Estado de la entrega (ENTREGADO, NO_ENTREGADO, etc.)
     * @param observaciones Observaciones sobre la entrega
     */
    public void registrarEntregaPaquete(String codigoRuta, Paquetes paquete, String estadoEntrega, String observaciones) {
        servicioRutas.registrarEntregaPaquete(codigoRuta, paquete, estadoEntrega, observaciones);
        controladorAuditoria.registrar("RUTAS", "ENTREGA", "Entrega registrada para paquete " + paquete.getCodigoSeguimiento() + ": " + estadoEntrega, USUARIO_SISTEMA);
    }
    /**
     * Finaliza una ruta liberando vehículo y conductor
     * @param codigoRuta Código de la ruta a finalizar
     * @param placaVehiculo Placa del vehículo asignado
     * @param idConductor Identificación del conductor asignado
     */
    public void finalizarRuta(String codigoRuta, String placaVehiculo, String idConductor) {
        servicioRutas.finalizarRuta(codigoRuta, placaVehiculo, idConductor);
        controladorAuditoria.registrar("RUTAS", "FIN", "Ruta finalizada: " + codigoRuta, USUARIO_SISTEMA);
    }
    /**
     * Lista todas las rutas activas (en proceso)
     * @return Lista de rutas con estado activo
     */
    public List<Rutas> listarRutasActivas() {
        return servicioRutas.listarRutasActivas();
    }
}