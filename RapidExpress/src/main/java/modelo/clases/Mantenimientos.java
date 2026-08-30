/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.clases;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author sergi
 */
public class Mantenimientos {
    private int id;
    private int vehiculoId;
    private String tipoMantenimiento;
    private String descripcion;
    private LocalDate fechaProgramada;
    private LocalDate fechaRealizacion;
    private double costo;
    public enum Estado {PROGRAMADO,EN_PROCESO,COMPLETADO,CANCELADO};
    private Estado estado;
    private String observaciones;
    private LocalDateTime fechaCreacion;

    public Mantenimientos(int id, int vehiculoId, String tipoMantenimiento, String descripcion, LocalDate fechaProgramada, LocalDate fechaRealizacion, double costo, Estado estado, String observaciones, LocalDateTime fechaCreacion) {
        this.id = id;
        this.vehiculoId = vehiculoId;
        this.tipoMantenimiento = tipoMantenimiento;
        this.descripcion = descripcion;
        this.fechaProgramada = fechaProgramada;
        this.fechaRealizacion = fechaRealizacion;
        this.costo = costo;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public int getVehiculoId() {
        return vehiculoId;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public LocalDate getFechaRealizacion() {
        return fechaRealizacion;
    }

    public double getCosto() {
        return costo;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public String toString() {
        return "Mantenimientos{" +
                "id=" + id +
                ", vehiculoId=" + vehiculoId +
                ", tipoMantenimiento='" + tipoMantenimiento + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaProgramada=" + fechaProgramada +
                ", fechaRealizacion=" + fechaRealizacion +
                ", costo=" + costo +
                ", estado=" + estado +
                ", observaciones='" + observaciones + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
