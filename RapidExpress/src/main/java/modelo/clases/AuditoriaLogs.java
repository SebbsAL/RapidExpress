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
public class AuditoriaLogs {
    private int id;
    private String entidad;
    private Integer entidadId; // Tipo Integer (objeto envoltorio) para permitir valores nulos/opcionales
    private String accion;
    private String detalle;
    private String usuarioOProceso;
    private LocalDateTime fechaEvento;

    public AuditoriaLogs(int id, String entidad, Integer entidadId, String accion,String detalle, String usuarioOProceso, LocalDateTime fechaEvento) {
        this.id = id;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.accion = accion;
        this.detalle = detalle;
        this.usuarioOProceso = usuarioOProceso;
        this.fechaEvento = fechaEvento;
    }

    public int getId() {
        return id;
    }

    public String getEntidad() {
        return entidad;
    }

    public Integer getEntidadId() {
        return entidadId;
    }

    public String getAccion() {
        return accion;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getUsuarioOProceso() {
        return usuarioOProceso;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    @Override
    public String toString() {
        return "AuditoriaLogs{" +
                "id=" + id +
                ", entidad='" + entidad + '\'' +
                ", entidadId=" + entidadId +
                ", accion='" + accion + '\'' +
                ", detalle='" + detalle + '\'' +
                ", usuarioOProceso='" + usuarioOProceso + '\'' +
                ", fechaEvento=" + fechaEvento +
                '}';
    }
}
