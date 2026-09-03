/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.util.List;
import modelo.clases.HistorialPaquetes;
import modelo.clases.Paquetes;
import modelo.servicios.ServicioPaquetes;
/**
 *
 * @author Sebastian
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
     * Registra un nuevo paquete en el sistema, creando u obteniendo al remitente y destinatario
     * @param descripcion Descripción del contenido del paquete
     * @param peso Peso del paquete en kg
     * @param dimensiones Dimensiones del paquete en formato "alto x ancho x largo" (cm)
     * @param dirOrigen Dirección de origen
     * @param dirDestino Dirección de destino
     * @return El código de seguimiento generado, o null si hubo error
     */
    public String registrarPaquete(String descripcion, double peso, String dimensiones, String dirOrigen, String dirDestino,
            String remitenteIdentificacion, String remitenteNombre, String remitenteTelefono, String remitenteEmail, String remitenteDireccion, String remitenteCiudad,
            String destinatarioIdentificacion, String destinatarioNombre, String destinatarioTelefono, String destinatarioEmail, String destinatarioDireccion, String destinatarioCiudad) {
        String trackingId = servicioPaquetes.registrarPaquete(descripcion, peso, dimensiones, dirOrigen, dirDestino,
                remitenteIdentificacion, remitenteNombre, remitenteTelefono, remitenteEmail, remitenteDireccion, remitenteCiudad,
                destinatarioIdentificacion, destinatarioNombre, destinatarioTelefono, destinatarioEmail, destinatarioDireccion, destinatarioCiudad);
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