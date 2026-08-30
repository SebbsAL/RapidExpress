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
public class HistorialPaquetes {
    private int id;
    private int paqueteId;    
    public enum Estado {EN_BODEGA,ASIGNADO_A_RUTA,EN_TRANSITO,ENTREGADO,DEVUELTO};
    private Estado estado;
    private String descripcionEvento;
    private String ubicacion;
    private LocalDateTime fechaRegistro;

    public HistorialPaquetes(int id, int paqueteId, Estado estado, String descripcionEvento,String ubicacion, LocalDateTime fechaRegistro) {
        this.id = id;
        this.paqueteId = paqueteId;
        this.estado = estado;
        this.descripcionEvento = descripcionEvento;
        this.ubicacion = ubicacion;
        this.fechaRegistro = fechaRegistro;
    }

    public int getId() {
        return id;
    }

    public int getPaqueteId() {
        return paqueteId;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    @Override
    public String toString() {
        return "HistorialPaquetes{" +
                "id=" + id +
                ", paqueteId=" + paqueteId +
                ", estado=" + estado +
                ", descripcionEvento='" + descripcionEvento + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
} 