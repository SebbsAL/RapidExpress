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
public class Vehiculos {
    
    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private int anio_fabricacion;
    private int capacidad_maxima_kg;
    private enum Estado {DISPONIBLE, EN_RUTA,EN_MANTENIMIENTO};
    private Estado estado; 
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion; 

    public Vehiculos(int id, String placa, String marca, String modelo, int anio_fabricacion, int capacidad_maxima_kg, Estado estado, LocalDateTime fecha_creacion, LocalDateTime fecha_actualizacion) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio_fabricacion = anio_fabricacion;
        this.capacidad_maxima_kg = capacidad_maxima_kg;
        this.estado = estado;
        this.fecha_creacion = fecha_creacion;
        this.fecha_actualizacion = fecha_actualizacion;
    }

    

    public int getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio_fabricacion() {
        return anio_fabricacion;
    }

    public int getCapacidad_maxima_kg() {
        return capacidad_maxima_kg;
    }
    
    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
    return "Vehiculos{" +
            "id=" + id +
            ", placa='" + placa + '\'' +
            ", marca='" + marca + '\'' +
            ", modelo='" + modelo + '\'' +
            ", anio_fabricacion=" + anio_fabricacion +
            ", capacidad_maxima_kg=" + capacidad_maxima_kg +
            ", estado=" + estado +
            ", fecha_creacion=" + fecha_creacion +
            ", fecha_actualizacion=" + fecha_actualizacion +
            '}';
    }
}
