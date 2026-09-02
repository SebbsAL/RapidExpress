package modelo.servicios;

import modelo.clases.Paquetes;
import modelo.clases.EstadoPaquete;
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

    public String registrarPaquete(String descripcion, double peso, String dimensiones, String dirOrigen, String dirDestino,
                                   String remitenteIdentificacion, String remitenteNombre, String remitenteTelefono, String remitenteEmail, String remitenteDireccion, String remitenteCiudad,
                                   String destinatarioIdentificacion, String destinatarioNombre, String destinatarioTelefono, String destinatarioEmail, String destinatarioDireccion, String destinatarioCiudad) {
        if (peso <= 0) {
            System.err.println("Error: El peso debe ser mayor a 0.");
            return null;
        }

        double[] dims;
        try {
            dims = parsearDimensiones(dimensiones);
        } catch (NumberFormatException e) {
            System.err.println("Error: Dimensiones invalidas. Use el formato alto x ancho x largo, por ejemplo 30x40x50.");
            return null;
        }
        double alto = dims[0];
        double ancho = dims[1];
        double largo = dims[2];

        Clientes remitente = servicioClientes.registrarOObtenerCliente(remitenteIdentificacion, remitenteNombre, remitenteTelefono, remitenteEmail, remitenteDireccion, remitenteCiudad);
        Clientes destinatario = servicioClientes.registrarOObtenerCliente(destinatarioIdentificacion, destinatarioNombre, destinatarioTelefono, destinatarioEmail, destinatarioDireccion, destinatarioCiudad);
        if (remitente == null || destinatario == null) {
            System.err.println("Error: No se pudo registrar u obtener el remitente/destinatario.");
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
        paquete.setEstado(EstadoPaquete.EN_BODEGA);

        daoPaquetes.insertar(paquete);

        // Obtenemos el paquete insertado para conocer su ID autogenerado
        Paquetes insertado = daoPaquetes.obtenerPorTracking(trackingId);
        if(insertado != null) {
            HistorialPaquetes historial = new HistorialPaquetes();
            historial.setPaqueteId(insertado.getId());
            historial.setEstado(EstadoPaquete.EN_BODEGA);
            historial.setDescripcionEvento("Ingresado en Bodega Central");
            historial.setUbicacion("Bodega Central");
            daoPaquetes.registrarHistorial(historial);
        }

        servicioAuditoria.registrarOperacionCritica("PAQUETES", "REGISTRO", "Paquete registrado: " + trackingId, "SISTEMA");

        return trackingId;
    }

    public Paquetes buscarPaquetePorTracking(String codigoSeguimiento) {
        Paquetes paquete = daoPaquetes.obtenerPorTracking(codigoSeguimiento);
        hidratarClientes(paquete);
        return paquete;
    }

    public List<HistorialPaquetes> consultarTrazabilidadPaquete(String codigoSeguimiento) {
        return daoPaquetes.obtenerHistorial(codigoSeguimiento);
    }

    public List<Paquetes> listarPaquetesEnBodega() {
        List<Paquetes> paquetes = daoPaquetes.obtenerPorEstado("EN_BODEGA");
        for (Paquetes paquete : paquetes) {
            hidratarClientes(paquete);
        }
        return paquetes;
    }

    private void hidratarClientes(Paquetes paquete) {
        if (paquete == null) {
            return;
        }
        paquete.setRemitente(servicioClientes.obtenerClientePorId(paquete.getRemitenteId()));
        paquete.setDestinatario(servicioClientes.obtenerClientePorId(paquete.getDestinatatioId()));
    }

    private double[] parsearDimensiones(String dimensiones) {
        String[] partes = dimensiones.split("[xX]");
        if (partes.length != 3) {
            throw new NumberFormatException("Formato de dimensiones inválido: " + dimensiones);
        }
        double alto = Double.parseDouble(partes[0].trim());
        double ancho = Double.parseDouble(partes[1].trim());
        double largo = Double.parseDouble(partes[2].trim());
        return new double[]{alto, ancho, largo};
    }
}
