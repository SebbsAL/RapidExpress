package com.rapidexpress.service;

import com.rapidexpress.model.entity.EstadoVehiculo;
import com.rapidexpress.model.entity.Vehiculos;
import com.rapidexpress.model.dao.IDaoVehiculos;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Servicio de gestión de vehículos.
 */
public class ServicioVehiculos {

    private final IDaoVehiculos daoVehiculos;

    public ServicioVehiculos(IDaoVehiculos daoVehiculos) {
        this.daoVehiculos = daoVehiculos;
    }

    /**
     * Registra un nuevo vehículo con estado inicial DISPONIBLE.
     */
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

    /**
     * Actualiza los datos de un vehículo existente.
     */
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

    /**
     * Lista todos los vehículos, ordenados por placa.
     */
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

    /**
     * Busca un vehículo por su placa.
     */
    public Vehiculos buscarVehiculoPorPlaca(String placa) {
        try {
            return daoVehiculos.obtenerPorPlaca(placa);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al buscar vehiculo: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lista los vehiculos DISPONIBLES con capacidad suficiente para la carga indicada.
     */
    public List<Vehiculos> ListarVehiculoPorAptitudes(double kg){
        try{
            return daoVehiculos.ListarVehiculoPorAptitudes(kg);
        }catch(SQLException e){
            System.err.println("Error de base de datos al buscar vehiculo: "+ e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene el resumen de cuantos vehiculos hay en cada estado.
     */
    public Map<String, Integer> ContabilizarEstados(){
        try {
            return daoVehiculos.ContabilizarEstados();
        }catch(SQLException e){
            System.err.println("Error de base de datos al obtener los estados: "+ e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene un vehículo a partir de su identificador interno.
     */
    public Vehiculos obtenerVehiculoPorId(int id) {
        try {
            return daoVehiculos.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al obtener vehiculo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza el estado de un vehículo (DISPONIBLE, EN_RUTA, etc.).
     */
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
