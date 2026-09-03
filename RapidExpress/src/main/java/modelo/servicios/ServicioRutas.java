package modelo.servicios;

import modelo.clases.Rutas;
import modelo.clases.EstadoRuta;
import modelo.clases.Paquetes;
import modelo.clases.EstadoPaquete;
import modelo.clases.RutaPaquetes;
import modelo.clases.EstadoEntrega;
import modelo.clases.Conductores;
import modelo.clases.Vehiculos;
import modelo.clases.EstadoVehiculo;
import modelo.clases.HistorialPaquetes;
import modelo.persistencia.IDaoRutas;
import modelo.persistencia.IDaoPaquetes;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServicioRutas {

    private final IDaoRutas daoRutas;
    private final ServicioVehiculos servicioVehiculos;
    private final ServicioConductores servicioConductores;
    private final IDaoPaquetes daoPaquetes;
    private final ServicioAuditoria servicioAuditoria;

    public ServicioRutas(IDaoRutas daoRutas, ServicioVehiculos servicioVehiculos, ServicioConductores servicioConductores,
                       IDaoPaquetes daoPaquetes, ServicioAuditoria servicioAuditoria) {
        this.daoRutas = daoRutas;
        this.servicioVehiculos = servicioVehiculos;
        this.servicioConductores = servicioConductores;
        this.daoPaquetes = daoPaquetes;
        this.servicioAuditoria = servicioAuditoria;
    }

    public String crearHojaDeRuta(String placaVehiculo, String identificacionConductor, List<String> codigosPaquetes) {
        if (codigosPaquetes == null || codigosPaquetes.isEmpty()) {
            System.err.println("Error: Debe especificar al menos un paquete para la ruta.");
            return null;
        }

        var vehiculo = servicioVehiculos.buscarVehiculoPorPlaca(placaVehiculo);
        var conductor = servicioConductores.buscarConductorPorIdentificacion(identificacionConductor);

        if (vehiculo == null || conductor == null) {
            System.err.println("Error: Vehiculo o conductor no encontrados.");
            return null;
        }

        try {
            List<Paquetes> paquetesSeleccionados = new ArrayList<>();
            for (String codigo : codigosPaquetes) {
                Paquetes p = daoPaquetes.obtenerPorTracking(codigo);
                if (p == null) {
                    System.err.println("Error: No se encontro el paquete con codigo " + codigo);
                    return null;
                }
                paquetesSeleccionados.add(p);
            }

            double pesoTotal = paquetesSeleccionados.stream()
                    .mapToDouble(Paquetes::getPesoKg)
                    .sum();

            if (pesoTotal > vehiculo.getCapacidad_maxima_kg()) {
                System.err.println("Error: El peso total (" + pesoTotal + "kg) excede la capacidad del vehiculo (" + vehiculo.getCapacidad_maxima_kg() + "kg).");
                return null;
            }

            String codigoRuta = "RUT-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

            Rutas ruta = new Rutas();
            ruta.setCodigoRuta(codigoRuta);
            ruta.setVehiculoId(vehiculo.getId());
            ruta.setConductorId(conductor.getId());
            ruta.setFechaRuta(LocalDate.now());
            ruta.setPesoTotalAsignadoKg(pesoTotal);
            ruta.setEstado(EstadoRuta.PLANIFICADA);

            int rutaId = daoRutas.insertar(ruta);
            if (rutaId <= 0) {
                System.err.println("Error: No se pudo crear la ruta " + codigoRuta + " (fallo al insertar en la base de datos).");
                return null;
            }

            int orden = 1;
            for (Paquetes p : paquetesSeleccionados) {
                daoRutas.asociarPaqueteARuta(rutaId, p.getId(), orden++);
                daoPaquetes.actualizarEstado(p.getCodigoSeguimiento(), EstadoPaquete.ASIGNADO_A_RUTA);

                HistorialPaquetes h = new HistorialPaquetes();
                h.setPaqueteId(p.getId());
                h.setEstado(EstadoPaquete.ASIGNADO_A_RUTA);
                h.setDescripcionEvento("Asignado a ruta " + codigoRuta);
                h.setUbicacion("Centro de Distribución");
                daoPaquetes.registrarHistorial(h);
            }

            servicioAuditoria.registrarOperacionCritica("RUTAS", "CREACION", "Ruta creada: " + codigoRuta + " con " + paquetesSeleccionados.size() + " paquetes.", "SISTEMA");
            return codigoRuta;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al crear la hoja de ruta: " + e.getMessage());
            return null;
        }
    }

    public boolean iniciarRuta(String codigoRuta) {
        try {
            Rutas ruta = daoRutas.obtenerPorCodigo(codigoRuta);
            if (ruta == null) {
                System.err.println("Error: No se encontro la ruta " + codigoRuta);
                return false;
            }
            Vehiculos vehiculo = servicioVehiculos.obtenerVehiculoPorId(ruta.getVehiculoId());
            Conductores conductor = servicioConductores.obtenerConductorPorId(ruta.getConductorId());
            if (vehiculo == null || conductor == null) {
                System.err.println("Error: No se pudo resolver el vehiculo o conductor de la ruta.");
                return false;
            }
            List<Paquetes> paquetesDeRuta = daoPaquetes.obtenerPorRuta(ruta.getId());

            daoRutas.actualizarEstado(codigoRuta, EstadoRuta.EN_PROCESO);
            servicioVehiculos.actualizarEstadoVehiculo(vehiculo.getPlaca(), EstadoVehiculo.EN_RUTA);

            for (Paquetes p : paquetesDeRuta) {
                daoPaquetes.actualizarEstado(p.getCodigoSeguimiento(), EstadoPaquete.EN_TRANSITO);

                HistorialPaquetes h = new HistorialPaquetes();
                h.setPaqueteId(p.getId());
                h.setEstado(EstadoPaquete.EN_TRANSITO);
                h.setDescripcionEvento("Vehículo en camino para entrega");
                h.setUbicacion("En Tránsito");
                daoPaquetes.registrarHistorial(h);
            }

            servicioAuditoria.registrarOperacionCritica("RUTAS", "INICIO", "Ruta en proceso: " + codigoRuta, "SISTEMA");
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al iniciar la ruta: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarEntregaPaquete(String codigoRuta, String codigoSeguimiento, String observaciones) {
        try {
            Rutas ruta = daoRutas.obtenerPorCodigo(codigoRuta);
            if (ruta == null) {
                System.err.println("Error: No se encontro la ruta " + codigoRuta);
                return false;
            }
            Paquetes paquete = daoPaquetes.obtenerPorTracking(codigoSeguimiento);
            if (paquete == null) {
                System.err.println("Error: No se encontro el paquete " + codigoSeguimiento);
                return false;
            }
            boolean actualizado = daoRutas.actualizarEstadoEntregaPaquete(codigoRuta, codigoSeguimiento, EstadoEntrega.ENTREGADO, observaciones);
            if (!actualizado) {
                System.err.println("Error: El paquete " + codigoSeguimiento + " no esta asignado a la ruta " + codigoRuta);
                return false;
            }
            daoPaquetes.actualizarEstado(codigoSeguimiento, EstadoPaquete.ENTREGADO);

            HistorialPaquetes h = new HistorialPaquetes();
            h.setPaqueteId(paquete.getId());
            h.setEstado(EstadoPaquete.ENTREGADO);
            h.setDescripcionEvento(observaciones);
            h.setUbicacion(paquete.getDireccionDestino());
            daoPaquetes.registrarHistorial(h);

            servicioAuditoria.registrarOperacionCritica("RUTAS", "ENTREGA", "Entrega registrada para paquete " + codigoSeguimiento + ": " + EstadoPaquete.ENTREGADO, "SISTEMA");
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al registrar la entrega: " + e.getMessage());
            return false;
        }
    }

    public boolean finalizarRuta(String codigoRuta) {
        try {
            Rutas ruta = daoRutas.obtenerPorCodigo(codigoRuta);
            if (ruta == null) {
                System.err.println("Error: No se encontro la ruta " + codigoRuta);
                return false;
            }
            Vehiculos vehiculo = servicioVehiculos.obtenerVehiculoPorId(ruta.getVehiculoId());
            Conductores conductor = servicioConductores.obtenerConductorPorId(ruta.getConductorId());
            if (vehiculo == null || conductor == null) {
                System.err.println("Error: No se pudo resolver el vehiculo o conductor de la ruta.");
                return false;
            }

            daoRutas.actualizarEstado(codigoRuta, EstadoRuta.COMPLETADA);
            servicioVehiculos.actualizarEstadoVehiculo(vehiculo.getPlaca(), EstadoVehiculo.DISPONIBLE);

            servicioAuditoria.registrarOperacionCritica("RUTAS", "FIN", "Ruta finalizada: " + codigoRuta, "SISTEMA");
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al finalizar la ruta: " + e.getMessage());
            return false;
        }
    }

    public List<RutaPaquetes> obtenerDetalleEntregas(String codigoRuta) {
        try {
            Rutas ruta = daoRutas.obtenerPorCodigo(codigoRuta);
            if (ruta == null) {
                System.err.println("Error: No se encontro la ruta " + codigoRuta);
                return new ArrayList<>();
            }
            List<RutaPaquetes> detalle = daoRutas.obtenerDetalleEntregas(ruta.getId());
            for (RutaPaquetes rp : detalle) {
                rp.setPaquete(daoPaquetes.obtenerPorId(rp.getPaqueteId()));
            }
            return detalle;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al obtener detalle de entregas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Rutas> listarRutasActivas() {
        try {
            List<Rutas> rutas = daoRutas.obtenerActivas();
            rutas.forEach(this::hidratarRuta);
            return rutas;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al listar rutas activas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void hidratarRuta(Rutas ruta) {
        ruta.setVehiculo(servicioVehiculos.obtenerVehiculoPorId(ruta.getVehiculoId()));
        ruta.setConductor(servicioConductores.obtenerConductorPorId(ruta.getConductorId()));
        try {
            ruta.setPaquetes(daoPaquetes.obtenerPorRuta(ruta.getId()));
        } catch (SQLException e) {
            System.err.println("Error de base de datos al obtener los paquetes de la ruta " + ruta.getCodigoRuta() + ": " + e.getMessage());
        }
    }
}
