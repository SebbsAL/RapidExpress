/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.model.entity;

/**
 * Resumen de una categoria de peso: cuantos paquetes tiene y cuanto pesan en
 * total. Es un resultado de solo lectura, por eso no expone setters.
 *
 * @author PC
 */
public class ResumenCategoriaPeso {
    private String categoria;
    private int cantidad;
    private double pesoTotal;

    public ResumenCategoriaPeso(String categoria, int cantidad, double pesoTotal) {
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.pesoTotal = pesoTotal;
    }

    public String getCategoria() { return categoria; }
    public int getCantidad() { return cantidad; }
    public double getPesoTotal() { return pesoTotal; }
}
