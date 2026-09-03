/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.controller;

import com.rapidexpress.service.ServicioAuditoria;

/**
 *
 * @author sergi 
 */
public class ControladorAuditoria {
    private final ServicioAuditoria servicioAuditoria;

    public ControladorAuditoria() {
        this(new ServicioAuditoria());
    }

    public ControladorAuditoria(ServicioAuditoria servicioAuditoria) {
        this.servicioAuditoria = servicioAuditoria;
    }
    
    /**
     * Registra una operación crítica en la auditoría del sistema.
     */
    public void registrar(String modulo, String accion, String detalle, String usuario){
        servicioAuditoria.registrarOperacionCritica(modulo, accion, detalle, usuario);
    }
}
