package modelo.persistencia;

import modelo.clases.EstadoPaquete;
import modelo.clases.HistorialPaquetes;
import modelo.clases.Paquetes;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de persistencia para Paquetes y su historial de trazabilidad.
 */
public interface IDaoPaquetes {
    void insertar(Paquetes paquete) throws SQLException;
    void actualizarEstado(String codigoSeguimiento, EstadoPaquete nuevoEstado) throws SQLException;
    Paquetes obtenerPorTracking(String codigoSeguimiento) throws SQLException;
    Paquetes obtenerPorId(int id) throws SQLException;
    List<Paquetes> obtenerPorEstado(String estado) throws SQLException;
    List<Paquetes> obtenerPorRuta(int rutaId) throws SQLException;
    void registrarHistorial(HistorialPaquetes historial) throws SQLException;
    List<HistorialPaquetes> obtenerHistorial(String codigoSeguimiento) throws SQLException;
}
