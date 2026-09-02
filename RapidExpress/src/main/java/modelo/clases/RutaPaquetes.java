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
public class RutaPaquetes {
   private int id;
    private int rutaId;
    private int paqueteId;
    private int ordenEntrega;
    private EstadoEntrega estadoEntrega;
    private LocalDateTime fechaEntregaEstimada;
    private LocalDateTime fechaEntregaReal;
    private String observacionesEntrega;
    private Paquetes paquete;

    public RutaPaquetes(int id, int rutaId, int paqueteId, int ordenEntrega,EstadoEntrega estadoEntrega, LocalDateTime fechaEntregaEstimada,LocalDateTime fechaEntregaReal, String observacionesEntrega) {
        this.id = id;
        this.rutaId = rutaId;
        this.paqueteId = paqueteId;
        this.ordenEntrega = ordenEntrega;
        this.estadoEntrega = estadoEntrega;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.fechaEntregaReal = fechaEntregaReal;
        this.observacionesEntrega = observacionesEntrega;
    }

    public RutaPaquetes() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRutaId() {
        return rutaId;
    }

    public void setRutaId(int rutaId) {
        this.rutaId = rutaId;
    }

    public int getPaqueteId() {
        return paqueteId;
    }

    public void setPaqueteId(int paqueteId) {
        this.paqueteId = paqueteId;
    }

    public int getOrdenEntrega() {
        return ordenEntrega;
    }

    public void setOrdenEntrega(int ordenEntrega) {
        this.ordenEntrega = ordenEntrega;
    }

    public EstadoEntrega getEstadoEntrega() {
        return this.estadoEntrega;
    }

    public void setEstadoEntrega(EstadoEntrega estadoEntrega) {
        this.estadoEntrega = estadoEntrega;
    }

    public LocalDateTime getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public LocalDateTime getFechaEntregaReal() {
        return fechaEntregaReal;
    }

    public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) {
        this.fechaEntregaReal = fechaEntregaReal;
    }

    public String getObservacionesEntrega() {
        return observacionesEntrega;
    }

    public void setObservacionesEntrega(String observacionesEntrega) {
        this.observacionesEntrega = observacionesEntrega;
    }

    public Paquetes getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquetes paquete) {
        this.paquete = paquete;
    }

    @Override
    public String toString() {
        return "RutaPaquetes{" +
                "id=" + id +
                ", rutaId=" + rutaId +
                ", paqueteId=" + paqueteId +
                ", ordenEntrega=" + ordenEntrega +
                ", estadoEntrega=" + estadoEntrega +
                ", fechaEntregaEstimada=" + fechaEntregaEstimada +
                ", fechaEntregaReal=" + fechaEntregaReal +
                ", observacionesEntrega='" + observacionesEntrega + '\'' +
                '}';
    } 
}
