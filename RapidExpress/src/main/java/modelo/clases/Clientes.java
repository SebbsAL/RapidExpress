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
public class Clientes {
    private int id;
    private String  numeroIdentificacion;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private String direccion;
    private String ciudad;
    private LocalDateTime fechaCreacion;

    public Clientes(int id, String numeroIdentificacion, String nombreCompleto, String telefono, String email, String direccion, String ciudad, LocalDateTime fechaCreacion) {
        this.id = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    @Override
    public String toString() {
        return "Clientes{" +
                "id=" + id +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
    
}
