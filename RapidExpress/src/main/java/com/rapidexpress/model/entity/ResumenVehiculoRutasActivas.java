/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.model.entity;

/**
 *
 * @author PC
 */
public class ResumenVehiculoRutasActivas {
    private String placa;
    private double capacidad_maxima_kg;
    private double pesoTotalAsignadoKg;
    
    public ResumenVehiculoRutasActivas(String placa, double capacidad_maxima_kg, double pesoTotalAsignadoKg) {
        this.placa = placa;
        this.capacidad_maxima_kg = capacidad_maxima_kg;
        this.pesoTotalAsignadoKg = pesoTotalAsignadoKg;
    }

    public double getCapacidad_maxima_kg() {return capacidad_maxima_kg;}
    public double getPesoTotalAsignadoKg() {return pesoTotalAsignadoKg;}
    public String getPlaca() {return placa;}

    public double getPorcentaje() {return (pesoTotalAsignadoKg / capacidad_maxima_kg) * 100;}
    
}
