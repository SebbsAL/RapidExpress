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
public class Paquetes {
    private int id;
    private String codigoSeguimiento;
    private String descripcionContenido;
    private double pesoKg;
    private double largoCm;
    private double anchoCm;
    private double altoCm;
    private double volumenM3;
    private String direccionOrigen;
    private String direccionDestino;
    private int remitenteId;
    private int destinatatioId;
    public enum Estado {EN_BODEGA, ASIGNADO_A_RUTA, EN_TRANSITO, ENTREGADO, DEVUELTO};
    private Estado estado; 
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Paquetes(int id, String codigoSeguimiento, String descripcionContenido, double pesoKg, double largoCm, double anchoCm, double altoCm, double volumenM3, String direccionOrigen, String direccionDestino, int remitenteId, int destinatatioId, Estado estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.codigoSeguimiento = codigoSeguimiento;
        this.descripcionContenido = descripcionContenido;
        this.pesoKg = pesoKg;
        this.largoCm = largoCm;
        this.anchoCm = anchoCm;
        this.altoCm = altoCm;
        this.volumenM3 = volumenM3;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.remitenteId = remitenteId;
        this.destinatatioId = destinatatioId;
        this.estado = estado;
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

    public String getCodigoSeguimiento() {
        return codigoSeguimiento;
    }

    public String getDescripcionContenido() {
        return descripcionContenido;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public double getLargoCm() {
        return largoCm;
    }

    public double getAnchoCm() {
        return anchoCm;
    }

    public double getAltoCm() {
        return altoCm;
    }

    public double getVolumenM3() {
        return volumenM3;
    }

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public int getRemitenteId() {
        return remitenteId;
    }

    public int getDestinatatioId() {
        return destinatatioId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    @Override
    public String toString() {
        return "Paquetes{" +
                "id=" + id +
                ", codigoSeguimiento='" + codigoSeguimiento + '\'' +
                ", descripcionContenido='" + descripcionContenido + '\'' +
                ", pesoKg=" + pesoKg +
                ", largoCm=" + largoCm +
                ", anchoCm=" + anchoCm +
                ", altoCm=" + altoCm +
                ", volumenM3=" + volumenM3 +
                ", direccionOrigen='" + direccionOrigen + '\'' +
                ", direccionDestino='" + direccionDestino + '\'' +
                ", remitenteId=" + remitenteId +
                ", destinatatioId=" + destinatatioId +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}
