/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.clases;

import java.time.LocalDateTime;

/**
 *
 * @author sergi
 */
public class AsignacionesVehiculoConductor {
    private int id;
    private int vehiculoId;
    private int conductorId;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaDesasignacion;
    private boolean activo;

    public AsignacionesVehiculoConductor(int id, int vehiculoId, int conductorId, LocalDateTime fechaAsignacion, LocalDateTime fechaDesasignacion, boolean activo) {
        this.id = id;
        this.vehiculoId = vehiculoId;
        this.conductorId = conductorId;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaDesasignacion = fechaDesasignacion;
        this.activo = activo;
    }
    
    public int getId() {
        return id;
    }

    public int getVehiculoId() {
        return vehiculoId;
    }

    public int getConductorId() {
        return conductorId;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public LocalDateTime getFechaDesasignacion() {
        return fechaDesasignacion;
    }

    public void setFechaDesasignacion(LocalDateTime fechaDesasignacion) {
        this.fechaDesasignacion = fechaDesasignacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "AsignacionesVehiculoConductor{" +
                "id=" + id +
                ", vehiculoId=" + vehiculoId +
                ", conductorId=" + conductorId +
                ", fechaAsignacion=" + fechaAsignacion +
                ", fechaDesasignacion=" + fechaDesasignacion +
                ", activo=" + activo +
                '}';
    }
}
