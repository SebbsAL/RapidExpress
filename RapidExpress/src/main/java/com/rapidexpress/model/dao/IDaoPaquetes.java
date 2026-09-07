package com.rapidexpress.model.dao;

import com.rapidexpress.model.entity.EstadoPaquete;
import com.rapidexpress.model.entity.HistorialPaquetes;
import com.rapidexpress.model.entity.Paquetes;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Contrato de persistencia para Paquetes y su historial de trazabilidad.
 */
public interface IDaoPaquetes {
    /** Inserta un nuevo paquete. */
    void insertar(Paquetes paquete) throws SQLException;
    /** Actualiza el estado de un paquete. */
    void actualizarEstado(String codigoSeguimiento, EstadoPaquete nuevoEstado) throws SQLException;
    /** Busca un paquete por su código de seguimiento. */
    Paquetes obtenerPorTracking(String codigoSeguimiento) throws SQLException;
    /** Busca un paquete por su id interno. */
    Paquetes obtenerPorId(int id) throws SQLException;
    /** Obtiene los paquetes que están en un estado dado. */
    List<Paquetes> obtenerPorEstado(String estado) throws SQLException;
    /** Obtiene los paquetes asignados a una ruta. */
    List<Paquetes> obtenerPorRuta(int rutaId) throws SQLException;
    /** Registra un evento en el historial de un paquete. */
    void registrarHistorial(HistorialPaquetes historial) throws SQLException;
    /** Obtiene el historial de eventos de un paquete. */
    List<HistorialPaquetes> obtenerHistorial(String codigoSeguimiento) throws SQLException;
    /** Busca paquetes que llevan mas de N dias sin actualizarse. */
    List<Paquetes> buscarPorDiasSinActualizar(int diasSinActualizar) throws SQLException;
    
    Map<String, Integer> contarPedidosPorRemitente() throws SQLException;

    /** Cuenta cuántos paquetes ha enviado (como remitente) el cliente con esa identificación. */
    int contarEnviadosPorIdentificacion(String identificacion) throws SQLException;
    /** Cuenta cuántos paquetes ha recibido (como destinatario) el cliente con esa identificación. */
    int contarRecibidosPorIdentificacion(String identificacion) throws SQLException;
}
