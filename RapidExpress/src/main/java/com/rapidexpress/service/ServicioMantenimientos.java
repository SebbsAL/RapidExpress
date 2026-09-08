package com.rapidexpress.service;

import com.rapidexpress.model.entity.Mantenimientos;
import com.rapidexpress.model.entity.EstadoMantenimiento;
import com.rapidexpress.model.entity.Vehiculos;
import com.rapidexpress.model.entity.EstadoVehiculo;
import com.rapidexpress.model.dao.IDaoMantenimientos;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio de gestión de mantenimientos de vehículos.
 */
public class ServicioMantenimientos {

    private final IDaoMantenimientos daoMantenimientos;
    private final ServicioVehiculos servicioVehiculos;

    public ServicioMantenimientos(IDaoMantenimientos daoMantenimientos, ServicioVehiculos servicioVehiculos) {
        this.daoMantenimientos = daoMantenimientos;
        this.servicioVehiculos = servicioVehiculos;
    }

    /**
     * Programa un mantenimiento para un vehículo DISPONIBLE.
     */
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

    /**
     * Actualiza estado, costo y observaciones de un mantenimiento; si queda
     * COMPLETADO, libera el vehículo dejándolo DISPONIBLE.
     */
    public boolean actualizarEstadoMantenimiento(int idMantenimiento, EstadoMantenimiento nuevoEstado, double costo, String observaciones, String placaVehiculo) {
        try {
            // Verifica que el mantenimiento realmente pertenezca al vehiculo indicado antes
            // de tocar nada; sin esto, un error de tipeo en la placa podria liberar (DISPONIBLE)
            // el vehiculo equivocado al completar el mantenimiento.
            Mantenimientos mantenimiento = daoMantenimientos.obtenerPorId(idMantenimiento);
            if (mantenimiento == null) {
                System.err.println("Error: No se encontro el mantenimiento con id " + idMantenimiento);
                return false;
            }
            Vehiculos vehiculo = servicioVehiculos.buscarVehiculoPorPlaca(placaVehiculo);
            if (vehiculo == null || mantenimiento.getVehiculoId() != vehiculo.getId()) {
                System.err.println("Error: El mantenimiento " + idMantenimiento + " no pertenece al vehiculo " + placaVehiculo);
                return false;
            }

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
    
    /**
     * Reprograma la fecha de un mantenimiento. Solo se permite si el mantenimiento
     * existe, sigue en estado PROGRAMADO (uno completado o cancelado ya no se
     * reagenda) y la nueva fecha no es anterior a hoy.
     */
    public boolean actualizarFechaMantenimiento(int idMantenimiento, LocalDate fechaReAgendar){
        try{
            Mantenimientos mantenimientoReagendar = daoMantenimientos.obtenerPorId(idMantenimiento);
            if (mantenimientoReagendar == null) {
                System.err.println("Error: No se encontro el mantenimiento con id " + idMantenimiento);
                return false;                
            }
            
            if (mantenimientoReagendar.getEstado() != EstadoMantenimiento.PROGRAMADO) {
                System.err.println("Error: el mantenimiento con id: " + idMantenimiento+" se no se puede re agendar, su estado actual es: "+mantenimientoReagendar.getEstado());
                return false;                
            }
            
            if (fechaReAgendar.isBefore(LocalDate.now())) {
                System.err.println("Error: el mantenimiento no puede ser re agendado al pasado.");
                return false;
            }
            
            boolean exito = daoMantenimientos.actualizarFechaMantenimiento(idMantenimiento, fechaReAgendar);
            
            return exito;
        }catch(SQLException ex){
            System.err.println("Error de base de datos al actualizar la fecha del mantenimiento : " + ex.getMessage() );
            return false;
        }        
    }
    
    /**
     * Obtiene el costo total en mantenimientos completados de cada vehiculo.
     */
    public Map<String, Double> totalGastadoEnMantenimientos(){
        try {
            return daoMantenimientos.totalGastadoEnMantenimientos();
        } catch (SQLException e) {
            System.err.println("Error de base de datos al consultar el total gastado: " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }

    /**
     * Lista todos los mantenimientos registrados, con su vehículo ya resuelto
     * para poder mostrar la placa en vez del id interno.
     */
    public List<Mantenimientos> listarMantenimientos() {
        try {
            List<Mantenimientos> mantenimientos = daoMantenimientos.obtenerTodos();
            mantenimientos.forEach(this::hidratarVehiculo);
            return mantenimientos;
        } catch (SQLException e) {
            System.err.println("Error de base de datos al listar mantenimientos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Completa un mantenimiento con los datos del vehículo al que pertenece.
     */
    private void hidratarVehiculo(Mantenimientos mantenimiento) {
        mantenimiento.setVehiculo(servicioVehiculos.obtenerVehiculoPorId(mantenimiento.getVehiculoId()));
    }

    /**
     * Consulta el historial de mantenimientos de un vehículo.
     */
    public List<Mantenimientos> consultarHistorialMantenimientosPorVehiculo(String placa) {
        try {
            return daoMantenimientos.obtenerPorPlacaVehiculo(placa);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al consultar historial de mantenimientos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
