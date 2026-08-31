package modelo.servicios;

import modelo.clases.Paquetes;
import modelo.clases.HistorialPaquetes;
import modelo.clases.Clientes;
import modelo.persistencia.DaoPaquetes;
import java.util.List;
import java.util.UUID;

public class ServicioPaquetes {

    private final DaoPaquetes daoPaquetes;
    private final ServicioAuditoria servicioAuditoria;
    private final ServicioClientes servicioClientes;

    public ServicioPaquetes(DaoPaquetes daoPaquetes, ServicioAuditoria servicioAuditoria, ServicioClientes servicioClientes) {
        this.daoPaquetes = daoPaquetes;
        this.servicioAuditoria = servicioAuditoria;
        this.servicioClientes = servicioClientes;
    }

    public String registrarPaquete(String descripcion, double peso, double largo, double ancho, double alto, 
                                   String dirOrigen, String dirDestino, Clientes remitente, Clientes destinatario) {
        if (peso <= 0) {
            System.err.println("Error: El peso debe ser mayor a 0.");
            return null;
        }

        String trackingId = "RPX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double volumen = (largo * ancho * alto) / 1000000.0; // asumiendo en metros cubicos

        Paquetes paquete = new Paquetes();
        paquete.setCodigoSeguimiento(trackingId);
        paquete.setDescripcionContenido(descripcion);
        paquete.setPesoKg(peso);
        paquete.setLargoCm(largo);
        paquete.setAnchoCm(ancho);
        paquete.setAltoCm(alto);
        paquete.setVolumenM3(volumen);
        paquete.setDireccionOrigen(dirOrigen);
        paquete.setDireccionDestino(dirDestino);
        paquete.setRemitenteId(remitente.getId());
        paquete.setDestinatatioId(destinatario.getId()); // Typo idéntico al de tu clase
        paquete.setEstado(Paquetes.Estado.EN_BODEGA);

        daoPaquetes.insertar(paquete);
        
        // Obtenemos el paquete insertado para conocer su ID autogenerado
        Paquetes insertado = daoPaquetes.obtenerPorTracking(trackingId);
        if(insertado != null) {
            HistorialPaquetes historial = new HistorialPaquetes();
            historial.setPaqueteId(insertado.getId());
            historial.setEstado(HistorialPaquetes.Estado.EN_BODEGA);
            historial.setDescripcionEvento("Ingresado en Bodega Central");
            historial.setUbicacion("Bodega Central");
            daoPaquetes.registrarHistorial(historial);
        }

        servicioAuditoria.registrarOperacionCritica("PAQUETES", "REGISTRO", "Paquete registrado: " + trackingId, "SISTEMA");
        
        return trackingId;
    }

    public Paquetes buscarPaquetePorTracking(String codigoSeguimiento) {
        return daoPaquetes.obtenerPorTracking(codigoSeguimiento);
    }

    public List<HistorialPaquetes> consultarTrazabilidadPaquete(String codigoSeguimiento) {
        return daoPaquetes.obtenerHistorial(codigoSeguimiento);
    }

    public List<Paquetes> listarPaquetesEnBodega() {
        return daoPaquetes.obtenerPorEstado("EN_BODEGA");
    }
}
