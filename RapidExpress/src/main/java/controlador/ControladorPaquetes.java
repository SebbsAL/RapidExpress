/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.List;
import modelo.clases.Clientes;
import modelo.clases.HistorialPaquetes;
import modelo.clases.Paquetes;
import modelo.servicios.ServicioPaquetes;

/**
 *
 * @author sergi
 */
public class ControladorPaquetes {
    private static final String USUARIO_SISTEMA = "SISTEMA";
    private final ServicioPaquetes servicioPaquetes;
    private final ControladorAuditoria controladorAuditoria;

    public ControladorPaquetes(ServicioPaquetes servicioPaquetes, ControladorAuditoria controladorAuditoria) {
        this.servicioPaquetes = servicioPaquetes;
        this.controladorAuditoria = controladorAuditoria;
    }
    
    /**
     * Registra un nuevo paquete en el sistema
     * @param descripcion Descripción del contenido del paquete
     * @param peso Peso del paquete en kg
     * @param largo Largo del paquete en cm
     * @param ancho Ancho del paquete en cm
     * @param alto Alto del paquete en cm
     * @param dirOrigen Dirección de origen
     * @param dirDestino Dirección de destino
     * @param remitente Cliente remitente
     * @param destinatario Cliente destinatario
     * @return El código de seguimiento generado, o null si hubo error
     */
    public String registrarPaquete(String descripcion, double peso, double largo, double ancho, double alto, 
                                   String dirOrigen, String dirDestino, Clientes remitente, Clientes destinatario) {
        String trackingId = servicioPaquetes.registrarPaquete(descripcion, peso, largo, ancho, alto, 
                                                               dirOrigen, dirDestino, remitente, destinatario);
        if (trackingId != null) {
            controladorAuditoria.registrar("PAQUETES", "REGISTRO", "Paquete registrado con tracking: " + trackingId, USUARIO_SISTEMA);
        }
        return trackingId;
    }
    
    /**
     * Busca un paquete por su código de seguimiento
     * @param codigoSeguimiento Código de tracking del paquete
     * @return Objeto Paquetes si existe, null si no se encuentra
     */
    public Paquetes buscarPaquetePorTracking(String codigoSeguimiento) {
        return servicioPaquetes.buscarPaquetePorTracking(codigoSeguimiento);
    }
    
    /**
     * Consulta la trazabilidad completa de un paquete
     * @param codigoSeguimiento Código de tracking del paquete
     * @return Lista de eventos del historial del paquete
     */
    public List<HistorialPaquetes> consultarTrazabilidadPaquete(String codigoSeguimiento) {
        return servicioPaquetes.consultarTrazabilidadPaquete(codigoSeguimiento);
    }
    
    /**
     * Lista todos los paquetes que están actualmente en bodega
     * @return Lista de paquetes con estado EN_BODEGA
     */
    public List<Paquetes> listarPaquetesEnBodega() {
        return servicioPaquetes.listarPaquetesEnBodega();
    }
}
