package modelo.servicios;

import modelo.clases.EstadoVehiculo;
import modelo.clases.Vehiculos;
import modelo.persistencia.DaoVehiculos;
import java.util.List;

public class ServicioVehiculos {

    private final DaoVehiculos daoVehiculos;

    public ServicioVehiculos(DaoVehiculos daoVehiculos) {
        this.daoVehiculos = daoVehiculos;
    }

    public void registrarVehiculo(String placa, String marca, String modelo, int anio, double capacidadMaxima) {
        if (anio < 1990) {
            System.err.println("Error: El año de fabricación debe ser mayor o igual a 1990.");
            return;
        }
        if (capacidadMaxima <= 0) {
            System.err.println("Error: La capacidad máxima debe ser mayor a 0.");
            return;
        }
        
        Vehiculos vehiculo = new Vehiculos();
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAnio_fabricacion(anio);
        vehiculo.setCapacidad_maxima_kg(capacidadMaxima);
        vehiculo.setEstado(EstadoVehiculo.DISPONIBLE);
        
        daoVehiculos.insertar(vehiculo);
        System.out.println("Vehículo registrado con éxito: " + placa);
    }

    public void actualizarDatosVehiculo(String placa, String marca, String modelo, int anio, double capacidadMaxima) {
        if (anio < 1990 || capacidadMaxima <= 0) {
            System.err.println("Error: Datos inválidos para actualizar el vehículo.");
            return;
        }
        Vehiculos vehiculo = new Vehiculos();
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAnio_fabricacion(anio);
        vehiculo.setCapacidad_maxima_kg(capacidadMaxima);
        
        daoVehiculos.actualizar(vehiculo);
        System.out.println("Datos del vehículo actualizados: " + placa);
    }

    public List<Vehiculos> listarVehiculos() {
        return daoVehiculos.obtenerTodos();
    }

    public Vehiculos buscarVehiculoPorPlaca(String placa) {
        return daoVehiculos.obtenerPorPlaca(placa);
    }

    public Vehiculos obtenerVehiculoPorId(int id) {
        return daoVehiculos.obtenerPorId(id);
    }

    public void actualizarEstadoVehiculo(String placa, EstadoVehiculo nuevoEstado) {
        Vehiculos v = daoVehiculos.obtenerPorPlaca(placa);
        if (v != null) {
            daoVehiculos.actualizarEstado(placa, nuevoEstado);
            System.out.println("Estado del vehículo " + placa + " actualizado a " + nuevoEstado);
        } else {
            System.err.println("Vehículo no encontrado.");
        }
    }
}
