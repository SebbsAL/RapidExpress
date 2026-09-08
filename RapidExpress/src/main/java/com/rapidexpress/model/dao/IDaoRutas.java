package com.rapidexpress.model.dao;

import com.rapidexpress.model.entity.EstadoEntrega;
import com.rapidexpress.model.entity.EstadoRuta;
import com.rapidexpress.model.entity.ResumenCategoriaPeso;
import com.rapidexpress.model.entity.ResumenVehiculoRutasActivas;
import com.rapidexpress.model.entity.RutaPaquetes;
import com.rapidexpress.model.entity.Rutas;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de persistencia para Rutas y su asociacion con Paquetes.
 */
public interface IDaoRutas {
    /** Inserta una nueva ruta y retorna su id generado. */
    int insertar(Rutas ruta) throws SQLException;
    /** Actualiza el estado de una ruta. */
    void actualizarEstado(String codigoRuta, EstadoRuta nuevoEstado) throws SQLException;
    /** Obtiene las rutas activas (en proceso). */
    List<Rutas> obtenerActivas() throws SQLException;
    /** Busca una ruta por su código. */
    Rutas obtenerPorCodigo(String codigoRuta) throws SQLException;
    /** Asocia un paquete a una ruta con su orden de entrega. */
    void asociarPaqueteARuta(int rutaId, int paqueteId, int ordenEntrega) throws SQLException;
    /** Actualiza el estado de entrega de un paquete dentro de una ruta. */
    boolean actualizarEstadoEntregaPaquete(String codigoRuta, String codigoSeguimiento, EstadoEntrega estadoEntrega, String observaciones) throws SQLException;
    /** Obtiene el detalle de entregas de una ruta. */
    List<RutaPaquetes> obtenerDetalleEntregas(int rutaId) throws SQLException;
    
    /** Obtiene todas las rutas en estado COMPLETADA. */
    List<Rutas> detallesRutasCompletadas() throws SQLException;
    
    /** Suma el peso asignado en rutas activas de cada vehiculo, junto con su capacidad. */
    List<ResumenVehiculoRutasActivas> reporteOcupacionDeVehiculos()throws SQLException;
}
