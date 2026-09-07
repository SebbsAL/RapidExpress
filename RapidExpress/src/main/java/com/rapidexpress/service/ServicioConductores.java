package com.rapidexpress.service;

import com.rapidexpress.model.entity.Conductores;
import com.rapidexpress.model.entity.EstadoConductor;
import com.rapidexpress.model.entity.EstadoVehiculo;
import com.rapidexpress.model.dao.IDaoConductores;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Servicio de gestión de conductores.
 */
public class ServicioConductores {

    private final IDaoConductores daoConductores;
    private final ServicioVehiculos servicioVehiculos;

    public ServicioConductores(IDaoConductores daoConductores, ServicioVehiculos servicioVehiculos) {
        this.daoConductores = daoConductores;
        this.servicioVehiculos = servicioVehiculos;
    }

    /**
     * Registra un nuevo conductor con estado inicial ACTIVO.
     */
    public void registrarConductor(String identificacion, String nombre, String licencia, String telefono, String email) {
        Conductores c = new Conductores();
        c.setNumeroIdentificacion(identificacion);
        c.setNombreCompleto(nombre);
        c.setTipoLicencia(licencia);
        c.setTelefono(telefono);
        c.setEmail(email);
        c.setEstado(EstadoConductor.ACTIVO);

        try {
            daoConductores.insertar(c);
            System.out.println("Conductor registrado: " + nombre);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al registrar conductor: " + e.getMessage());
        }
    }

    /**
     * Actualiza los datos personales y de licencia de un conductor existente.
     */
    public boolean actualizarDatosConductor(String identificacion, String nombre, String licencia, String telefono, String email) {
        Conductores c = new Conductores();
        c.setNumeroIdentificacion(identificacion);
        c.setNombreCompleto(nombre);
        c.setTipoLicencia(licencia);
        c.setTelefono(telefono);
        c.setEmail(email);

        try {
            boolean actualizado = daoConductores.actualizar(c);
            if (!actualizado) {
                System.err.println("Error: No se encontro el conductor con identificacion " + identificacion);
                return false;
            }
            System.out.println("Datos del conductor actualizados.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al actualizar conductor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos los conductores registrados, ordenados por nombre.
     */
    public List<Conductores> listarConductores() {
        try {
            List<Conductores> conductores = daoConductores.obtenerTodos();
            conductores.sort(Comparator.comparing(Conductores::getNombreCompleto));
            return conductores;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al listar conductores: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Conductores> conductoresSinVehiculoAsignado(){
        try {
            List<Conductores> conductoresSinVehAsignado = daoConductores.conductoresSinVehiculoAsignado();
            return conductoresSinVehAsignado;
        } catch (Exception e) {
            System.err.println("Error de base de datos al listar conductores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca un conductor por su número de identificación.
     */
    public Conductores buscarConductorPorIdentificacion(String identificacion) {
        try {
            return daoConductores.obtenerPorIdentificacion(identificacion);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al buscar conductor: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene un conductor a partir de su identificador interno.
     */
    public Conductores obtenerConductorPorId(int id) {
        try {
            return daoConductores.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al obtener conductor: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza el estado de un conductor (ACTIVO, EN_RUTA, INACTIVO, etc.).
     */
    public boolean actualizarEstadoConductor(String identificacion, EstadoConductor nuevoEstado) {
        try {
            boolean actualizado = daoConductores.actualizarEstado(identificacion, nuevoEstado);
            if (!actualizado) {
                System.err.println("Error: No se encontro el conductor con identificacion " + identificacion);
                return false;
            }
            System.out.println("Estado del conductor actualizado a " + nuevoEstado);
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al actualizar estado del conductor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Asigna un vehículo disponible a un conductor activo.
     */
    public boolean asignarVehiculoAConductor(String identificacionConductor, String placaVehiculo) {
        Conductores conductor = buscarConductorPorIdentificacion(identificacionConductor);
        if (conductor == null || conductor.getEstado() != EstadoConductor.ACTIVO) {
            System.err.println("Error: Conductor no existe o no esta ACTIVO.");
            return false;
        }

        var vehiculo = servicioVehiculos.buscarVehiculoPorPlaca(placaVehiculo);
        if (vehiculo == null || vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            System.err.println("Error: Vehiculo no existe o no esta DISPONIBLE.");
            return false;
        }

        try {
            if (daoConductores.tieneAsignacionActiva(identificacionConductor)) {
                System.err.println("Error: El conductor ya tiene un vehiculo asignado activo.");
                return false;
            }
            if (daoConductores.vehiculoTieneAsignacionActiva(vehiculo.getId())) {
                System.err.println("Error: El vehiculo " + placaVehiculo + " ya tiene un conductor asignado activo.");
                return false;
            }

            daoConductores.registrarAsignacion(vehiculo.getId(), conductor.getId());
            System.out.println("Vehiculo " + placaVehiculo + " asignado exitosamente al conductor " + identificacionConductor);
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al asignar vehiculo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Termina la asignación de vehículo activa de un conductor, dejándolo libre
     * para asignarse a otro vehículo más adelante.
     */
    public boolean desasignarVehiculoDeConductor(String identificacionConductor) {
        Conductores conductor = buscarConductorPorIdentificacion(identificacionConductor);
        if (conductor == null) {
            System.err.println("Error: Conductor no existe.");
            return false;
        }
        try {
            boolean desasignado = daoConductores.desasignarVehiculo(identificacionConductor);
            if (!desasignado) {
                System.err.println("Error: El conductor " + identificacionConductor + " no tiene una asignacion activa.");
                return false;
            }
            System.out.println("Conductor " + identificacionConductor + " desasignado de su vehiculo.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al desasignar vehiculo: " + e.getMessage());
            return false;
        }
    }
}
