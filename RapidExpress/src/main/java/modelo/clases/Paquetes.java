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
    private int destinatarioId;
    private EstadoPaquete estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Clientes remitente;
    private Clientes destinatario;

    public Paquetes(int id, String codigoSeguimiento, String descripcionContenido, double pesoKg, double largoCm, double anchoCm, double altoCm, double volumenM3, String direccionOrigen, String direccionDestino, int remitenteId, int destinatarioId, EstadoPaquete estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
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
        this.destinatarioId = destinatarioId;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Paquetes() {
    }
    
        
    public EstadoPaquete getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoPaquete estado) {
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

    public double getPeso() {
        return pesoKg;
    }

    public String getDimensiones() {
        return largoCm + "x" + anchoCm + "x" + altoCm + " cm";
    }

    public Clientes getRemitente() {
        return remitente;
    }

    public void setRemitente(Clientes remitente) {
        this.remitente = remitente;
    }

    public Clientes getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Clientes destinatario) {
        this.destinatario = destinatario;
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

    public int getDestinatarioId() {
        return destinatarioId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setCodigoSeguimiento(String codigoSeguimiento) {
        this.codigoSeguimiento = codigoSeguimiento;
    }

    public void setDescripcionContenido(String descripcionContenido) {
        this.descripcionContenido = descripcionContenido;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public void setLargoCm(double largoCm) {
        this.largoCm = largoCm;
    }

    public void setAnchoCm(double anchoCm) {
        this.anchoCm = anchoCm;
    }

    public void setAltoCm(double altoCm) {
        this.altoCm = altoCm;
    }

    public void setVolumenM3(double volumenM3) {
        this.volumenM3 = volumenM3;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public void setRemitenteId(int remitenteId) {
        this.remitenteId = remitenteId;
    }

    public void setDestinatarioId(int destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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
                ", destinatarioId=" + destinatarioId +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}
