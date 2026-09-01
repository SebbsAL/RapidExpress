package modelo.servicios;

import modelo.clases.Rutas;
import modelo.clases.Paquetes;
import modelo.clases.Conductores;
import modelo.clases.Vehiculos;
import modelo.clases.HistorialPaquetes;
import modelo.persistencia.DaoRutas;
import modelo.persistencia.DaoPaquetes;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public String crearHojaDeRuta(String placaVehiculo, String identificacionConductor, List<String> codigosPaquetes) {
        var vehiculo = servicioVehiculos.buscarVehiculoPorPlaca(placaVehiculo);
        var conductor = servicioConductores.buscarConductorPorIdentificacion(identificacionConductor);

        if (vehiculo == null || conductor == null) {
            System.err.println("Error: Vehículo o conductor no encontrados.");
            return null;
        }

        List<Paquetes> paquetesSeleccionados = new ArrayList<>();
        for (String codigo : codigosPaquetes) {
            Paquetes p = daoPaquetes.obtenerPorTracking(codigo);
            if (p == null) {
                System.err.println("Error: No se encontró el paquete con código " + codigo);
                return null;
            }
            paquetesSeleccionados.add(p);
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

    public boolean iniciarRuta(String codigoRuta) {
        Rutas ruta = daoRutas.obtenerPorCodigo(codigoRuta);
        if (ruta == null) {
            System.err.println("Error: No se encontró la ruta " + codigoRuta);
            return false;
        }
        Vehiculos vehiculo = servicioVehiculos.obtenerVehiculoPorId(ruta.getVehiculoId());
        Conductores conductor = servicioConductores.obtenerConductorPorId(ruta.getConductorId());
        if (vehiculo == null || conductor == null) {
            System.err.println("Error: No se pudo resolver el vehículo o conductor de la ruta.");
            return false;
        }
        List<Paquetes> paquetesDeRuta = daoPaquetes.obtenerPorRuta(ruta.getId());

        daoRutas.actualizarEstado(codigoRuta, "EN_PROCESO");
        servicioVehiculos.actualizarEstadoVehiculo(vehiculo.getPlaca(), "EN_RUTA");
        servicioConductores.actualizarEstadoConductor(conductor.getNumeroIdentificacion(), "EN_RUTA");

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
        return true;
    }

    public boolean registrarEntregaPaquete(String codigoRuta, String codigoSeguimiento, String observaciones) {
        Paquetes paquete = daoPaquetes.obtenerPorTracking(codigoSeguimiento);
        if (paquete == null) {
            System.err.println("Error: No se encontró el paquete " + codigoSeguimiento);
            return false;
        }
        String estadoEntrega = "ENTREGADO";
        daoRutas.actualizarEstadoEntregaPaquete(codigoRuta, codigoSeguimiento, estadoEntrega, observaciones);
        daoPaquetes.actualizarEstado(codigoSeguimiento, estadoEntrega);

        HistorialPaquetes h = new HistorialPaquetes();
        h.setPaqueteId(paquete.getId());
        h.setEstado(HistorialPaquetes.Estado.valueOf(estadoEntrega));
        h.setDescripcionEvento(observaciones);
        h.setUbicacion(paquete.getDireccionDestino());
        daoPaquetes.registrarHistorial(h);

        servicioAuditoria.registrarOperacionCritica("RUTAS", "ENTREGA", "Entrega registrada para paquete " + codigoSeguimiento + ": " + estadoEntrega, "SISTEMA");
        return true;
    }

    public boolean finalizarRuta(String codigoRuta) {
        Rutas ruta = daoRutas.obtenerPorCodigo(codigoRuta);
        if (ruta == null) {
            System.err.println("Error: No se encontró la ruta " + codigoRuta);
            return false;
        }
        Vehiculos vehiculo = servicioVehiculos.obtenerVehiculoPorId(ruta.getVehiculoId());
        Conductores conductor = servicioConductores.obtenerConductorPorId(ruta.getConductorId());
        if (vehiculo == null || conductor == null) {
            System.err.println("Error: No se pudo resolver el vehículo o conductor de la ruta.");
            return false;
        }

        daoRutas.actualizarEstado(codigoRuta, "COMPLETADA");
        servicioVehiculos.actualizarEstadoVehiculo(vehiculo.getPlaca(), "DISPONIBLE");
        servicioConductores.actualizarEstadoConductor(conductor.getNumeroIdentificacion(), "ACTIVO");

        servicioAuditoria.registrarOperacionCritica("RUTAS", "FIN", "Ruta finalizada: " + codigoRuta, "SISTEMA");
        return true;
    }

    public List<Rutas> listarRutasActivas() {
        List<Rutas> rutas = daoRutas.obtenerActivas();
        for (Rutas ruta : rutas) {
            ruta.setVehiculo(servicioVehiculos.obtenerVehiculoPorId(ruta.getVehiculoId()));
            ruta.setConductor(servicioConductores.obtenerConductorPorId(ruta.getConductorId()));
            ruta.setPaquetes(daoPaquetes.obtenerPorRuta(ruta.getId()));
        }
        return rutas;
    }
}
