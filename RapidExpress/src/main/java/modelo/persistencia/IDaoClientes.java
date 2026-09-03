package modelo.persistencia;

import modelo.clases.Clientes;
import java.sql.SQLException;

/**
 * Contrato de persistencia para Clientes.
 */
public interface IDaoClientes {
    Clientes insertar(Clientes cliente) throws SQLException;
    Clientes obtenerPorIdentificacion(String identificacion) throws SQLException;
    Clientes obtenerPorId(int id) throws SQLException;
}
