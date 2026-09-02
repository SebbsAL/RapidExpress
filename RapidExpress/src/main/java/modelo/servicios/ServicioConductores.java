package modelo.servicios;

import modelo.clases.Conductores;
import modelo.clases.EstadoConductor;
import modelo.clases.EstadoVehiculo;
import modelo.persistencia.DaoConductores;
import java.util.List;

public class ServicioConductores {

    private final DaoConductores daoConductores;
    private final ServicioVehiculos servicioVehiculos;

    public ServicioConductores(DaoConductores daoConductores, ServicioVehiculos servicioVehiculos) {
        this.daoConductores = daoConductores;
        this.servicioVehiculos = servicioVehiculos;
    } 

    public void registrarConductor(String identificacion, String nombre, String licencia, String telefono, String email) {
        Conductores c = new Conductores();
        c.setNumeroIdentificacion(identificacion);
        c.setNombreCompleto(nombre);
        c.setTipoLicencia(licencia);
        c.setTelefono(telefono);
        c.setEmail(email);
        c.setEstado(EstadoConductor.ACTIVO);
        
        daoConductores.insertar(c);
        System.out.println("Conductor registrado: " + nombre);
    }

    public void actualizarDatosConductor(String identificacion, String nombre, String licencia, String telefono, String email) {
        Conductores c = new Conductores();
        c.setNumeroIdentificacion(identificacion);
        c.setNombreCompleto(nombre);
        c.setTipoLicencia(licencia);
        c.setTelefono(telefono);
        c.setEmail(email);
        
        daoConductores.actualizar(c);
        System.out.println("Datos del conductor actualizados.");
    }

    public List<Conductores> listarConductores() {
        return daoConductores.obtenerTodos();
    }

    public Conductores buscarConductorPorIdentificacion(String identificacion) {
        return daoConductores.obtenerPorIdentificacion(identificacion);
    }

    public Conductores obtenerConductorPorId(int id) {
        return daoConductores.obtenerPorId(id);
    }

    public void actualizarEstadoConductor(String identificacion, EstadoConductor nuevoEstado) {
        daoConductores.actualizarEstado(identificacion, nuevoEstado);
        System.out.println("Estado del conductor actualizado a " + nuevoEstado);
    }

    public void asignarVehiculoAConductor(String identificacionConductor, String placaVehiculo) {
        Conductores conductor = buscarConductorPorIdentificacion(identificacionConductor);
        if (conductor == null || conductor.getEstado() != EstadoConductor.ACTIVO) {
            System.err.println("Error: Conductor no existe o no esta ACTIVO.");
            return;
        }

        var vehiculo = servicioVehiculos.buscarVehiculoPorPlaca(placaVehiculo);
        if (vehiculo == null || vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            System.err.println("Error: Vehiculo no existe o no esta DISPONIBLE.");
            return;
        }

        if (daoConductores.tieneAsignacionActiva(identificacionConductor)) {
            System.err.println("Error: El conductor ya tiene un vehiculo asignado activo.");
            return;
        }

        daoConductores.registrarAsignacion(vehiculo.getId(), conductor.getId());
        System.out.println("Vehiculo " + placaVehiculo + " asignado exitosamente al conductor " + identificacionConductor);
    }
}
