package modelo.servicios;

import modelo.clases.Rutas;
import modelo.clases.Paquetes;
import modelo.clases.HistorialPaquetes;
import modelo.persistencia.DaoRutas;
import modelo.persistencia.DaoPaquetes;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ServicioRutas {

    private final DaoRutas daoRutas;
    private final ServicioVehiculos servicioVehiculos;
    private final ServicioConductores servicioConductores;
    private final DaoPaquetes daoPaquetes;
    private final ServicioAuditoria servicioAuditoria;

    public ServicioRutas(DaoRutas daoRutas, ServicioVehiculos servicioVehiculos, ServicioConductores servicioConductores, 
                       DaoPaquetes daoPaquetes, ServicioAuditoria servicioAuditoria) {
        this.daoRutas = daoRutas;
        this.servicioVehiculos = servicioVehiculos;
        this.servicioConductores = servicioConductores;
        this.daoPaquetes = daoPaquetes;
        this.servicioAuditoria = servicioAuditoria;
    }

    public String crearHojaDeRuta(String placaVehiculo, String identificacionConductor, List<Paquetes> paquetesSeleccionados) {
        var vehiculo = servicioVehiculos.buscarVehiculoPorPlaca(placaVehiculo);
        var conductor = servicioConductores.buscarConductorPorIdentificacion(identificacionConductor);

        if (vehiculo == null || conductor == null) {
            System.err.println("Error: Vehículo o conductor no encontrados.");
            return null;
        }

        double pesoTotal = 0;
        for (Paquetes p : paquetesSeleccionados) {
            pesoTotal += p.getPesoKg();
        }

        if (pesoTotal > vehiculo.getCapacidad_maxima_kg()) {
            System.err.println("Error: El peso total (" + pesoTotal + "kg) excede la capacidad del vehículo (" + vehiculo.getCapacidad_maxima_kg() + "kg).");
            return null;
        }

        String codigoRuta = "RUT-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Rutas ruta = new Rutas();
        ruta.setCodigoRuta(codigoRuta);
        ruta.setVehiculoId(vehiculo.getId());
        ruta.setConductorId(conductor.getId());
        ruta.setFechaRuta(LocalDate.now());
        ruta.setPesoTotalAsignadoKg(pesoTotal);
        ruta.setEstado(Rutas.Estado.PLANIFICADA);

        int rutaId = daoRutas.insertar(ruta);

        int orden = 1;
        for (Paquetes p : paquetesSeleccionados) {
            daoRutas.asociarPaqueteARuta(rutaId, p.getId(), orden++);
            daoPaquetes.actualizarEstado(p.getCodigoSeguimiento(), "ASIGNADO_A_RUTA");
            
            HistorialPaquetes h = new HistorialPaquetes();
            h.setPaqueteId(p.getId());
            h.setEstado(HistorialPaquetes.Estado.ASIGNADO_A_RUTA);
            h.setDescripcionEvento("Asignado a ruta " + codigoRuta);
            h.setUbicacion("Centro de Distribución");
            daoPaquetes.registrarHistorial(h);
        }

        servicioAuditoria.registrarOperacionCritica("RUTAS", "CREACION", "Ruta creada: " + codigoRuta + " con " + paquetesSeleccionados.size() + " paquetes.", "SISTEMA");
        return codigoRuta;
    }

    public void iniciarRuta(String codigoRuta, List<Paquetes> paquetesDeRuta, String placaVehiculo, String idConductor) {
        daoRutas.actualizarEstado(codigoRuta, "EN_PROCESO");
        servicioVehiculos.actualizarEstadoVehiculo(placaVehiculo, "EN_RUTA");
        servicioConductores.actualizarEstadoConductor(idConductor, "EN_RUTA");

        for (Paquetes p : paquetesDeRuta) {
            daoPaquetes.actualizarEstado(p.getCodigoSeguimiento(), "EN_TRANSITO");
            
            HistorialPaquetes h = new HistorialPaquetes();
            h.setPaqueteId(p.getId());
            h.setEstado(HistorialPaquetes.Estado.EN_TRANSITO);
            h.setDescripcionEvento("Vehículo en camino para entrega");
            h.setUbicacion("En Tránsito");
            daoPaquetes.registrarHistorial(h);
        }
        
        servicioAuditoria.registrarOperacionCritica("RUTAS", "INICIO", "Ruta en proceso: " + codigoRuta, "SISTEMA");
    }

    public void registrarEntregaPaquete(String codigoRuta, Paquetes paquete, String estadoEntrega, String observaciones) {
        daoRutas.actualizarEstadoEntregaPaquete(codigoRuta, paquete.getCodigoSeguimiento(), estadoEntrega, observaciones);
        daoPaquetes.actualizarEstado(paquete.getCodigoSeguimiento(), estadoEntrega);
        
        HistorialPaquetes h = new HistorialPaquetes();
        h.setPaqueteId(paquete.getId());
        h.setEstado(HistorialPaquetes.Estado.valueOf(estadoEntrega));
        h.setDescripcionEvento(observaciones);
        h.setUbicacion(estadoEntrega.equals("ENTREGADO") ? paquete.getDireccionDestino() : "Retorno a Bodega");
        daoPaquetes.registrarHistorial(h);
    }

    public void finalizarRuta(String codigoRuta, String placaVehiculo, String idConductor) {
        daoRutas.actualizarEstado(codigoRuta, "COMPLETADA");
        servicioVehiculos.actualizarEstadoVehiculo(placaVehiculo, "DISPONIBLE");
        servicioConductores.actualizarEstadoConductor(idConductor, "ACTIVO");
        
        servicioAuditoria.registrarOperacionCritica("RUTAS", "FIN", "Ruta finalizada: " + codigoRuta, "SISTEMA");
    }
    
    public List<Rutas> listarRutasActivas() {
        return daoRutas.obtenerActivas();
    }
}
