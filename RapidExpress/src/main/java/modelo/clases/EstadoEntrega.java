package modelo.clases;

/**
 * Estados de entrega de un paquete dentro de una ruta (tabla ruta_paquetes).
 * Debe coincidir con el ENUM de ruta_paquetes.estado_entrega en 1_schema_ddl.sql.
 * 
 */
public enum EstadoEntrega {
    PENDIENTE,
    ENTREGADO,
    DEVUELTO,
    INCIDENCIA
}
