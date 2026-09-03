package modelo.servicios;

import modelo.clases.EstadoVehiculo;
import modelo.clases.Vehiculos;
import modelo.persistencia.IDaoVehiculos;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServicioVehiculos {

    private final IDaoVehiculos daoVehiculos;

    public ServicioVehiculos(IDaoVehiculos daoVehiculos) {
        this.daoVehiculos = daoVehiculos;
    }

    public boolean registrarVehiculo(String placa, String marca, String modelo, int anio, double capacidadMaxima) {
        if (anio < 1990) {
            System.err.println("Error: El ano de fabricacion debe ser mayor o igual a 1990.");
            return false;
        }
        if (capacidadMaxima <= 0) {
            System.err.println("Error: La capacidad maxima debe ser mayor a 0.");
            return false;
        }

        Vehiculos vehiculo = new Vehiculos();
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAnio_fabricacion(anio);
        vehiculo.setCapacidad_maxima_kg(capacidadMaxima);
        vehiculo.setEstado(EstadoVehiculo.DISPONIBLE);

        try {
            daoVehiculos.insertar(vehiculo);
            System.out.println("Vehiculo registrado con exito: " + placa);
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al registrar vehiculo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarDatosVehiculo(String placa, String marca, String modelo, int anio, double capacidadMaxima) {
        if (anio < 1990 || capacidadMaxima <= 0) {
            System.err.println("Error: Datos invalidos para actualizar el vehiculo.");
            return false;
        }
        Vehiculos vehiculo = new Vehiculos();
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAnio_fabricacion(anio);
        vehiculo.setCapacidad_maxima_kg(capacidadMaxima);

        try {
            boolean actualizado = daoVehiculos.actualizar(vehiculo);
            if (!actualizado) {
                System.err.println("Error: No se encontro el vehiculo con placa " + placa);
                return false;
            }
            System.out.println("Datos del vehiculo actualizados: " + placa);
            return true;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al actualizar vehiculo: " + e.getMessage());
            return false;
        }
    }

    public List<Vehiculos> listarVehiculos() {
        try {
            List<Vehiculos> vehiculos = daoVehiculos.obtenerTodos();
            vehiculos.sort(Comparator.comparing(Vehiculos::getPlaca));
            return vehiculos;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al listar vehiculos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Vehiculos buscarVehiculoPorPlaca(String placa) {
        try {
            return daoVehiculos.obtenerPorPlaca(placa);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al buscar vehiculo: " + e.getMessage());
            return null;
        }
    }

    public Vehiculos obtenerVehiculoPorId(int id) {
        try {
            return daoVehiculos.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al obtener vehiculo: " + e.getMessage());
            return null;
        }
    }

    public boolean actualizarEstadoVehiculo(String placa, EstadoVehiculo nuevoEstado) {
        try {
            Vehiculos v = daoVehiculos.obtenerPorPlaca(placa);
            if (v != null) {
                daoVehiculos.actualizarEstado(placa, nuevoEstado);
                System.out.println("Estado del vehiculo " + placa + " actualizado a " + nuevoEstado);
                return true;
            } else {
                System.err.println("Vehiculo no encontrado.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos al actualizar estado del vehiculo: " + e.getMessage());
            return false;
        }
    }
}
