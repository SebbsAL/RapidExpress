/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.servicios.ServicioAuditoria;

/**
 *
 * @author sergi
 */
public class ControladorAuditoria {
    private final ServicioAuditoria servicioAuditoria;

    public ControladorAuditoria(ServicioAuditoria servicioAuditoria) {
        this.servicioAuditoria = servicioAuditoria;
    }
    
    public void registrar(String modulo, String accion, String detalle, String usuario){
        servicioAuditoria.registrarOperacionCritica(modulo, accion, detalle, usuario);
    }
}
