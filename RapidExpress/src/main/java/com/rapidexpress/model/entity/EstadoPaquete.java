package modelo.clases;

/**
 * Estados del ciclo de vida de un paquete. Compartido por {@link Paquetes} 
 * y {@link HistorialPaquetes} (antes eran dos enums idénticos duplicados).
 */
public enum EstadoPaquete {
    EN_BODEGA,
    ASIGNADO_A_RUTA,
    EN_TRANSITO,
    ENTREGADO,
    DEVUELTO
}
