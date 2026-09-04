package com.rapidexpress.model.dao;

import java.sql.SQLException;

/**
 * Contrato de persistencia para el registro de auditoría en base de datos
 * (tabla auditoria_logs).
 */
public interface IDaoAuditoria {
    /** Inserta un registro de auditoría en la tabla auditoria_logs. */
    void registrar(String entidad, String accion, String detalle, String usuarioOProceso) throws SQLException;
}
