/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.List;
import modelo.clases.Vehiculos;
import modelo.clases.EstadoVehiculo;
import modelo.servicios.ServicioVehiculos;
/**
 * Controlador para la gestión de vehículos del sistema.
 * Maneja las operaciones CRUD y cambios de estado de vehículos, 
 * registrando todas las operaciones en la auditoría del sistema.
 *
 */
public class VehiculoController {
    private static final String USUARIO_SISTEMA = "SISTEMA";
    private ServicioVehiculos servicioVehiculos;
    private ControladorAuditoria controladorAuditoria;
    public VehiculoController(ServicioVehiculos servicioVehiculos, ControladorAuditoria controladorAuditoria) {
        this.servicioVehiculos = servicioVehiculos;
        this.controladorAuditoria = controladorAuditoria;
    }
    /**
     * Registra un nuevo vehículo en el sistema.
     *
     * @param placa Placa del vehículo
     * @param marca Marca del vehículo
     * @param modelo Modelo del vehículo
     * @param anio Año de fabricación
     * @param capacidadMaxima Capacidad máxima de carga en kg
     */
    public void registrarVehiculo(String placa, String marca, String modelo, int anio, double capacidadMaxima){
        servicioVehiculos.registrarVehiculo(placa, marca, modelo, anio, capacidadMaxima);
        controladorAuditoria.registrar("VEHICULOS", "CREACION", "Vehículo registrado: " + placa, USUARIO_SISTEMA);
    }
    /**
     * Actualiza los datos de un vehículo existente.
     *
     * @param placa Placa del vehículo
     * @param marca Marca del vehículo
     * @param modelo Modelo del vehículo
     * @param anio Año de fabricación
     * @param capacidadMaxima Capacidad máxima de carga en kg
     */
    public void actualizarDatosVehiculo(String placa, String marca, String modelo, int anio, double capacidadMaxima){
        servicioVehiculos.actualizarDatosVehiculo(placa, marca, modelo, anio, capacidadMaxima);
        controladorAuditoria.registrar("VEHICULOS", "ACTUALIZACION", "Vehículo actualizado: " + placa, USUARIO_SISTEMA);
    }
    /**
     * Lista todos los vehículos registrados en el sistema.
     *
     * @return Lista de vehículos
     */
    public List<Vehiculos> listarVehiculos(){
        return servicioVehiculos.listarVehiculos();
    }
    /**
     * Busca un vehículo por su placa.
     *
     * @param placa Placa del vehículo a buscar
     * @return El vehículo encontrado o null si no existe
     */
    public Vehiculos buscarVehiculoPorPlaca(String placa){
        return servicioVehiculos.buscarVehiculoPorPlaca(placa);
    }
    /**
     * Actualiza el estado de un vehículo (DISPONIBLE, EN_MANTENIMIENTO, EN_RUTA, etc.).
     *
     * @param placa Placa del vehículo
     * @param nuevoEstado Nuevo estado del vehículo
     */
    public void actualizarEstadoVehiculo(String placa, EstadoVehiculo nuevoEstado){
        servicioVehiculos.actualizarEstadoVehiculo(placa, nuevoEstado);
        controladorAuditoria.registrar("VEHICULOS", "CAMBIO ESTADO", "Vehículo " + placa + " cambió a estado " + nuevoEstado, USUARIO_SISTEMA);
    }

}


