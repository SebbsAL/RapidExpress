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
    private double capacidad_maxima_kg;
    private EstadoVehiculo estado;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    public Vehiculos(int id, String placa, String marca, String modelo, int anio_fabricacion, int capacidad_maxima_kg, EstadoVehiculo estado, LocalDateTime fecha_creacion, LocalDateTime fecha_actualizacion) {
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

    public Vehiculos() {
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

    public String getMarca() {
        return marca;
    }    
    
    public int getAnio_fabricacion() {
        return anio_fabricacion;
    }

    public double getCapacidad_maxima_kg() {
        return capacidad_maxima_kg;
    }
    
    public EstadoVehiculo getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAnio_fabricacion(int anio_fabricacion) {
        this.anio_fabricacion = anio_fabricacion;
    }

    public void setCapacidad_maxima_kg(double capacidad_maxima_kg) {
        this.capacidad_maxima_kg = capacidad_maxima_kg;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public void setFecha_actualizacion(LocalDateTime fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
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
