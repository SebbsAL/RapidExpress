package modelo.servicios;

import modelo.clases.Mantenimientos;
import modelo.clases.EstadoMantenimiento;
import modelo.clases.Vehiculos;
import modelo.clases.EstadoVehiculo;
import modelo.persistencia.IDaoMantenimientos;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServicioMantenimientos {

    private final IDaoMantenimientos daoMantenimientos;
    private final ServicioVehiculos servicioVehiculos;

    public ServicioMantenimientos(IDaoMantenimientos daoMantenimientos, ServicioVehiculos servicioVehiculos) {
        this.daoMantenimientos = daoMantenimientos;
        this.servicioVehiculos = servicioVehiculos;
    }

    public boolean programarMantenimiento(String placaVehiculo, String tipo, String descripcion, LocalDate fechaProgramada) {
        Vehiculos v = servicioVehiculos.buscarVehiculoPorPlaca(placaVehiculo);
        if (v == null) {
            System.err.println("Error: El vehiculo no existe.");
            return false;
        }
        if (v.getEstado() != EstadoVehiculo.DISPONIBLE) {
            System.err.println("Error: El vehiculo debe estar DISPONIBLE para programar mantenimiento.");
            return false;
        }

        Mantenimientos m = new Mantenimientos();
        m.setVehiculoId(v.getId());
        m.setTipoMantenimiento(tipo);
        m.setDescripcion(descripcion);
        m.setFechaProgramada(fechaProgramada);
        m.setEstado(EstadoMantenimiento.PROGRAMADO);

        try {
            daoMantenimientos.insertar(m);
            servicioVehiculos.actualizarEstadoVehiculo(placaVehiculo, EstadoVehiculo.EN_MANTENIMIENTO);
            System.out.println("Mantenimiento programado exitosamente para el vehiculo " + placaVehiculo);
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al programar mantenimiento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarEstadoMantenimiento(int idMantenimiento, EstadoMantenimiento nuevoEstado, double costo, String observaciones, String placaVehiculo) {
        try {
            boolean actualizado = daoMantenimientos.actualizarEstadoYCostos(idMantenimiento, nuevoEstado, costo, observaciones);
            if (!actualizado) {
                System.err.println("Error: No se encontro el mantenimiento con id " + idMantenimiento);
                return false;
            }

            if (nuevoEstado == EstadoMantenimiento.COMPLETADO) {
                servicioVehiculos.actualizarEstadoVehiculo(placaVehiculo, EstadoVehiculo.DISPONIBLE);
                System.out.println("Mantenimiento completado. Vehiculo " + placaVehiculo + " esta DISPONIBLE.");
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al actualizar mantenimiento: " + e.getMessage());
            return false;
        }
    }

    public List<Mantenimientos> consultarHistorialMantenimientosPorVehiculo(String placa) {
        try {
            return daoMantenimientos.obtenerPorPlacaVehiculo(placa);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al consultar historial de mantenimientos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
