package modelo.servicios;

import modelo.clases.Mantenimientos;
import modelo.clases.EstadoMantenimiento;
import modelo.clases.Vehiculos;
import modelo.clases.EstadoVehiculo;
import modelo.persistencia.DaoMantenimientos;
import java.time.LocalDate;
import java.util.List;

public class ServicioMantenimientos {

    private final DaoMantenimientos daoMantenimientos;
    private final ServicioVehiculos servicioVehiculos;

    public ServicioMantenimientos(DaoMantenimientos daoMantenimientos, ServicioVehiculos servicioVehiculos) {
        this.daoMantenimientos = daoMantenimientos;
        this.servicioVehiculos = servicioVehiculos;
    }

    public void programarMantenimiento(String placaVehiculo, String tipo, String descripcion, LocalDate fechaProgramada) {
        Vehiculos v = servicioVehiculos.buscarVehiculoPorPlaca(placaVehiculo);
        if (v == null) {
            System.err.println("Error: El vehículo no existe.");
            return;
        }
        if (v.getEstado() != EstadoVehiculo.DISPONIBLE) {
            System.err.println("Error: El vehículo debe estar DISPONIBLE para programar mantenimiento.");
            return;
        }

        Mantenimientos m = new Mantenimientos();
        m.setVehiculoId(v.getId());
        m.setTipoMantenimiento(tipo);
        m.setDescripcion(descripcion);
        m.setFechaProgramada(fechaProgramada);
        m.setEstado(EstadoMantenimiento.PROGRAMADO);

        daoMantenimientos.insertar(m);
        servicioVehiculos.actualizarEstadoVehiculo(placaVehiculo, EstadoVehiculo.EN_MANTENIMIENTO);
        System.out.println("Mantenimiento programado exitosamente para el vehículo " + placaVehiculo);
    }

    public void actualizarEstadoMantenimiento(int idMantenimiento, EstadoMantenimiento nuevoEstado, double costo, String observaciones, String placaVehiculo) {
        daoMantenimientos.actualizarEstadoYCostos(idMantenimiento, nuevoEstado, costo, observaciones);

        if (nuevoEstado == EstadoMantenimiento.COMPLETADO) {
            servicioVehiculos.actualizarEstadoVehiculo(placaVehiculo, EstadoVehiculo.DISPONIBLE);
            System.out.println("Mantenimiento completado. Vehículo " + placaVehiculo + " está DISPONIBLE.");
        }
    }

    public List<Mantenimientos> consultarHistorialMantenimientosPorVehiculo(String placa) {
        return daoMantenimientos.obtenerPorPlacaVehiculo(placa);
    }
}
