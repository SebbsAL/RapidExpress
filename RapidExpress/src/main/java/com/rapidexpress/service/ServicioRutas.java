package com.rapidexpress.service;

import com.rapidexpress.model.entity.Rutas;
import com.rapidexpress.model.entity.EstadoRuta;
import com.rapidexpress.model.entity.Paquetes;
import com.rapidexpress.model.entity.EstadoPaquete;
import com.rapidexpress.model.entity.RutaPaquetes;
import com.rapidexpress.model.entity.EstadoEntrega;
import com.rapidexpress.model.entity.Conductores;
import com.rapidexpress.model.entity.EstadoConductor;
import com.rapidexpress.model.entity.Vehiculos;
import com.rapidexpress.model.entity.EstadoVehiculo;
import com.rapidexpress.model.entity.HistorialPaquetes;
import com.rapidexpress.model.dao.IDaoRutas;
import com.rapidexpress.model.dao.IDaoPaquetes;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de rutas de entrega.
 */
public class ServicioRutas {

    private final IDaoRutas daoRutas;
    private final ServicioVehiculos servicioVehiculos;
    private final ServicioConductores servicioConductores;
    private final IDaoPaquetes daoPaquetes;

    // La auditoria de cada operacion de rutas la hace ControladorRutas (una sola vez);
    // este Servicio ya no la duplica.
    public ServicioRutas(IDaoRutas daoRutas, ServicioVehiculos servicioVehiculos, ServicioConductores servicioConductores,
                       IDaoPaquetes daoPaquetes) {
        this.daoRutas = daoRutas;
        this.servicioVehiculos = servicioVehiculos;
        this.servicioConductores = servicioConductores;
        this.daoPaquetes = daoPaquetes;
    }

    /**
     * Crea una hoja de ruta validando vehículo, conductor y capacidad de carga.
     */
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
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            System.err.println("Error: El vehiculo " + placaVehiculo + " no esta DISPONIBLE (estado actual: " + vehiculo.getEstado() + ").");
            return null;
        }
        if (conductor.getEstado() != EstadoConductor.ACTIVO) {
            System.err.println("Error: El conductor " + identificacionConductor + " no esta ACTIVO (estado actual: " + conductor.getEstado() + ").");
            return null;
        }

        try {
            List<Paquetes> paquetesSeleccionados = codigosPaquetes.stream() // abre un flujo funcional sobre la lista de codigos recibidos.
                    .map(this::buscarPaqueteOReportarError) // por cada codigo del flujo, llama al helper de abajo y lo reemplaza por el Paquetes encontrado (o por null si no existe;
            //                                            el helper ya imprime el error especifico de ese codigo).
                    .collect(Collectors.toList()); // junta todos los resultados del flujo en una List<Paquetes> nueva,
            //                                            en el mismo orden en que se ingresaron los codigos.

            // Si algun codigo no se pudo resolver a un paquete real, paquetesSeleccionados quedo con un hueco (null)
            // en esa posicion; en ese caso no se puede armar la ruta.
            if (paquetesSeleccionados.contains(null)) {
                return null;
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

            return codigoRuta;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al crear la hoja de ruta: " + e.getMessage());
            return null;
        }
    }

    // Helper usado desde el stream de crearHojaDeRuta (via "this::buscarPaqueteOReportarError").
    // Existe como metodo aparte, en vez de una llamada directa a daoPaquetes.obtenerPorTracking,
    // porque ese metodo del DAO declara "throws SQLException" y una referencia a metodo usada en
    // un stream no puede propagar excepciones checked: hay que atraparla aqui adentro.
    private Paquetes buscarPaqueteOReportarError(String codigo) {
        try {
            // Le pide al DAO el paquete real correspondiente a ese codigo de seguimiento.
            Paquetes p = daoPaquetes.obtenerPorTracking(codigo);
            // Si no existe, se avisa aqui mismo cual codigo especifico fue el que fallo
            // (el stream que llama a este metodo ya no sabe, por si solo, cual codigo era).
            if (p == null) {
                System.err.println("Error: No se encontro el paquete con codigo " + codigo);
                return null;
            }
            // Un paquete ya asignado a otra ruta, en transito, entregado o devuelto no puede
            // volver a agregarse a una ruta nueva: solo los que siguen EN_BODEGA califican.
            if (p.getEstado() != EstadoPaquete.EN_BODEGA) {
                System.err.println("Error: El paquete " + codigo + " no esta EN_BODEGA (estado actual: " + p.getEstado() + "), no se puede agregar a una ruta.");
                return null;
            }
            // Devuelve el paquete encontrado y disponible.
            return p;
        } catch (SQLException e) {
            // Un error real de base de datos se reporta igual que un paquete no encontrado,
            // para que el stream de crearHojaDeRuta lo detecte de la misma forma (con un null).
            System.err.println("Error de base de datos al buscar el paquete " + codigo + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Cancela una ruta que todavía no ha iniciado, devolviendo sus paquetes a EN_BODEGA.
     * Solo se puede cancelar una ruta en estado PLANIFICADA: una vez iniciada, vehículo,
     * conductor y paquetes ya están comprometidos y se debe usar finalizarRuta.
     */
    public boolean cancelarRuta(String codigoRuta) {
        try {
            Rutas ruta = daoRutas.obtenerPorCodigo(codigoRuta);
            if (ruta == null) {
                System.err.println("Error: No se encontro la ruta " + codigoRuta);
                return false;
            }
            if (ruta.getEstado() != EstadoRuta.PLANIFICADA) {
                System.err.println("Error: Solo se puede cancelar una ruta en estado PLANIFICADA (estado actual: " + ruta.getEstado() + ").");
                return false;
            }

            List<Paquetes> paquetesDeRuta = daoPaquetes.obtenerPorRuta(ruta.getId());
            for (Paquetes p : paquetesDeRuta) {
                daoPaquetes.actualizarEstado(p.getCodigoSeguimiento(), EstadoPaquete.EN_BODEGA);

                HistorialPaquetes h = new HistorialPaquetes();
                h.setPaqueteId(p.getId());
                h.setEstado(EstadoPaquete.EN_BODEGA);
                h.setDescripcionEvento("Ruta " + codigoRuta + " cancelada; paquete devuelto a bodega");
                h.setUbicacion("Centro de Distribución");
                daoPaquetes.registrarHistorial(h);
            }

            daoRutas.actualizarEstado(codigoRuta, EstadoRuta.CANCELADA);
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al cancelar la ruta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inicia una ruta planificada y pone en tránsito vehículo, conductor y paquetes.
     */
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
            servicioConductores.actualizarEstadoConductor(conductor.getNumeroIdentificacion(), EstadoConductor.EN_RUTA);

            for (Paquetes p : paquetesDeRuta) {
                daoPaquetes.actualizarEstado(p.getCodigoSeguimiento(), EstadoPaquete.EN_TRANSITO);

                HistorialPaquetes h = new HistorialPaquetes();
                h.setPaqueteId(p.getId());
                h.setEstado(EstadoPaquete.EN_TRANSITO);
                h.setDescripcionEvento("Vehículo en camino para entrega");
                h.setUbicacion("En Tránsito");
                daoPaquetes.registrarHistorial(h);
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al iniciar la ruta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra el resultado de la entrega de un paquete dentro de una ruta.
     */
    public boolean registrarEntregaPaquete(String codigoRuta, String codigoSeguimiento, String observaciones, EstadoEntrega estadoEntrega) {
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
            boolean actualizado = daoRutas.actualizarEstadoEntregaPaquete(codigoRuta, codigoSeguimiento, estadoEntrega, observaciones);
            if (!actualizado) {
                System.err.println("Error: El paquete " + codigoSeguimiento + " no esta asignado a la ruta " + codigoRuta);
                return false;
            }

            // ENTREGADO/DEVUELTO cierran el ciclo del paquete; una INCIDENCIA no cambia
            // su estado general (sigue EN_TRANSITO hasta que se resuelva).
            EstadoPaquete nuevoEstadoPaquete = switch (estadoEntrega) {
                case ENTREGADO -> EstadoPaquete.ENTREGADO;
                case DEVUELTO -> EstadoPaquete.DEVUELTO;
                default -> null;
            };
            if (nuevoEstadoPaquete != null) {
                daoPaquetes.actualizarEstado(codigoSeguimiento, nuevoEstadoPaquete);
            }

            String ubicacion = switch (estadoEntrega) {
                case ENTREGADO -> paquete.getDireccionDestino();
                case DEVUELTO -> "Retorno a Bodega";
                default -> "En Transito - Incidencia reportada";
            };

            HistorialPaquetes h = new HistorialPaquetes();
            h.setPaqueteId(paquete.getId());
            h.setEstado(nuevoEstadoPaquete != null ? nuevoEstadoPaquete : paquete.getEstado());
            h.setDescripcionEvento(estadoEntrega == EstadoEntrega.INCIDENCIA ? "Incidencia en la entrega: " + observaciones : observaciones);
            h.setUbicacion(ubicacion);
            daoPaquetes.registrarHistorial(h);

            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al registrar la entrega: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finaliza una ruta en proceso y libera al vehículo y al conductor.
     */
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
            servicioConductores.actualizarEstadoConductor(conductor.getNumeroIdentificacion(), EstadoConductor.ACTIVO);

            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al finalizar la ruta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el detalle de entrega de cada paquete asignado a una ruta.
     */
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
    
    /**
     * Calcula, sobre las rutas COMPLETADAS, el peso promedio transportado y la
     * duracion promedio en minutos. Retorna {pesoPromedio, duracionPromedio,
     * cantidadDeRutas}: el tercer valor permite a la Vista distinguir "no hay
     * rutas" de "el promedio dio cero".
     */
    public double[] calcularPromediosRutasCompletadas(){
        try{
        
        List<Rutas> rutasCompletadas = daoRutas.detallesRutasCompletadas();
        double pesoPromedio = rutasCompletadas.stream().mapToDouble(ruta -> ruta.getPesoTotalAsignadoKg()).average().orElse(0);
        double duracionPromedio = rutasCompletadas.stream().mapToDouble(ruta -> Duration.between(ruta.getHoraInicio(), ruta.getHoraFin()).toMinutes()).average().orElse(0);
        double[] pesoYduracionPromedio = {pesoPromedio,duracionPromedio,rutasCompletadas.size()};
        return pesoYduracionPromedio;
        }catch(SQLException e){
            System.err.println("Error de base de datos al obtener detalle de rutas completadas: " + e.getMessage());
            double[] pesoYduracionPromedio={0,0,0};
            return pesoYduracionPromedio;
        }
    }

    /**
     * Lista las rutas actualmente activas (en proceso).
     */
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

    /**
     * Completa una ruta con sus datos de vehículo, conductor y paquetes.
     */
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
