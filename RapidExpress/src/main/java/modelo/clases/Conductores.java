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
public class Conductores {
    private int id;
    private String numeroIdentificacion;
    private String nombreCompleto;
    private String tipoLicencia;
    private String telefono;
    private String email;
    public enum Estado {ACTIVO,DE_VACACIONES,INACTIVO};
    private Estado estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime  fechaActualizacion;

    public Conductores(int id, String numeroIdentificacion, String nombreCompleto, String tipoLicencia, String telefono, String email, Estado estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombreCompleto = nombreCompleto;
        this.tipoLicencia = tipoLicencia;
        this.telefono = telefono;
        this.email = email;
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

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    @Override
    public String toString() {
        return "Conductores{" +
                "id=" + id +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", tipoLicencia='" + tipoLicencia + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
    
    
}


