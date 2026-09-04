package com.rapidexpress.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementación JDBC del acceso a datos de auditoría (tabla auditoria_logs).
 */
public class DaoAuditoria implements IDaoAuditoria {
    /** Inserta un registro de auditoría en la tabla auditoria_logs. */
    public void registrar(String entidad, String accion, String detalle, String usuarioOProceso) throws SQLException {
        String sql = "INSERT INTO auditoria_logs (entidad, accion, detalle, usuario_o_proceso) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad);
            ps.setString(2, accion);
            ps.setString(3, detalle);
            ps.setString(4, usuarioOProceso);
            ps.executeUpdate();
        }
    }
}
