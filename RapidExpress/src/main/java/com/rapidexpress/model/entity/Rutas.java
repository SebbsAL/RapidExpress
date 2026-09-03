/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sergi
 */
public class Rutas extends EntidadBase {
    private String codigoRuta;
    private int vehiculoId;
    private int conductorId;
    private LocalDate fechaRuta;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double pesoTotalAsignadoKg;
    private EstadoRuta estado;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Vehiculos vehiculo;
    private Conductores conductor;
    private List<Paquetes> paquetes;

    public Rutas(int id, String codigoRuta, int vehiculoId, int conductorId, LocalDate fechaRuta, LocalTime horaInicio, LocalTime horaFin, double pesoTotalAsignadoKg, EstadoRuta estado, String observaciones, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.codigoRuta = codigoRuta;
        this.vehiculoId = vehiculoId;
        this.conductorId = conductorId;
        this.fechaRuta = fechaRuta;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.pesoTotalAsignadoKg = pesoTotalAsignadoKg;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Rutas() {
    }
    
    public EstadoRuta getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoRuta estado) {
        this.estado = estado;
    }

    public String getCodigoRuta() {
        return codigoRuta;
    }

    public int getVehiculoId() {
        return vehiculoId;
    }

    public int getConductorId() {
        return conductorId;
    }

    public LocalDate getFechaRuta() {
        return fechaRuta;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public double getPesoTotalAsignadoKg() {
        return pesoTotalAsignadoKg;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setCodigoRuta(String codigoRuta) {
        this.codigoRuta = codigoRuta;
    }

    public void setVehiculoId(int vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public void setConductorId(int conductorId) {
        this.conductorId = conductorId;
    }

    public void setFechaRuta(LocalDate fechaRuta) {
        this.fechaRuta = fechaRuta;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public void setPesoTotalAsignadoKg(double pesoTotalAsignadoKg) {
        this.pesoTotalAsignadoKg = pesoTotalAsignadoKg;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Vehiculos getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculos vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Conductores getConductor() {
        return conductor;
    }

    public void setConductor(Conductores conductor) {
        this.conductor = conductor;
    }

    public List<Paquetes> getPaquetes() {
        return paquetes == null ? null : new ArrayList<>(paquetes);
    }

    public void setPaquetes(List<Paquetes> paquetes) {
        this.paquetes = paquetes == null ? null : new ArrayList<>(paquetes);
    }

    @Override
    public String toString() {
        return "Rutas{" +
                "id=" + id +
                ", codigoRuta='" + codigoRuta + '\'' +
                ", vehiculoId=" + vehiculoId +
                ", conductorId=" + conductorId +
                ", fechaRuta=" + fechaRuta +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", pesoTotalAsignadoKg=" + pesoTotalAsignadoKg +
                ", estado=" + estado +
                ", observaciones='" + observaciones + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}
