/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.clases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author sergi
 */
public class Rutas {
    private int id;
    private String codigoRuta;
    private int vehiculoId;
    private int conductorId;
    private LocalDate fechaRuta;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double pesoTotalAsignadoKg;
    public enum Estado {PLANIFICADA,EN_PROCESO,COMPLETADA,CANCELADA}
    private Estado estado;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Rutas(int id, String codigoRuta, int vehiculoId, int conductorId, LocalDate fechaRuta, LocalTime horaInicio, LocalTime horaFin, double pesoTotalAsignadoKg, Estado estado, String observaciones, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
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
    
    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
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
