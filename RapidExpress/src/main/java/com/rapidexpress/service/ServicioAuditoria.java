package com.rapidexpress.service;

import com.rapidexpress.model.dao.IDaoAuditoria;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registra las operaciones críticas del sistema en dos lugares independientes:
 * el archivo de texto (requisito explícito del enunciado, y la bitácora que
 * sobrevive si la base de datos falla) y la tabla auditoria_logs de MySQL
 * (para poder consultarlas con SQL). Si uno de los dos falla, el otro igual
 * se intenta.
 */
public class ServicioAuditoria {

    private static final String LOG_FILE_PATH = "rapidexpress_audit.log";
    private final IDaoAuditoria daoAuditoria;

    public ServicioAuditoria(IDaoAuditoria daoAuditoria) {
        this.daoAuditoria = daoAuditoria;
    }

    /**
     * Escribe una entrada de auditoría en el archivo de log y en la base de datos.
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

        try {
            daoAuditoria.registrar(modulo, accion, detalle, usuario);
        } catch (SQLException e) {
            System.err.println("Error de base de datos al registrar auditoria: " + e.getMessage());
        }
    }
}
