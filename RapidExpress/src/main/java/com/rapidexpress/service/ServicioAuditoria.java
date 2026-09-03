package com.rapidexpress.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registra en un archivo de log las operaciones críticas del sistema.
 */
public class ServicioAuditoria {

    private static final String LOG_FILE_PATH = "rapidexpress_audit.log";

    /**
     * Escribe una entrada de auditoría en el archivo de log.
     */
    public void registrarOperacionCritica(String modulo, String accion, String detalle, String usuario) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("[%s] [MODULO: %s] [ACCION: %s] [User: %s] - %s\n", 
                                        timestamp, modulo, accion, usuario, detalle);
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Error critico guardando en archivo de auditoria: " + e.getMessage());
        }
    }
}
