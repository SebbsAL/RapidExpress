package modelo.persistencia;

import modelo.clases.EstadoEntrega;
import modelo.clases.EstadoRuta;
import modelo.clases.RutaPaquetes;
import modelo.clases.Rutas;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de persistencia para Rutas y su asociacion con Paquetes.
 */
public interface IDaoRutas {
    int insertar(Rutas ruta) throws SQLException;
    void actualizarEstado(String codigoRuta, EstadoRuta nuevoEstado) throws SQLException;
    List<Rutas> obtenerActivas() throws SQLException;
    Rutas obtenerPorCodigo(String codigoRuta) throws SQLException;
    void asociarPaqueteARuta(int rutaId, int paqueteId, int ordenEntrega) throws SQLException;
    boolean actualizarEstadoEntregaPaquete(String codigoRuta, String codigoSeguimiento, EstadoEntrega estadoEntrega, String observaciones) throws SQLException;
    List<RutaPaquetes> obtenerDetalleEntregas(int rutaId) throws SQLException;
}
