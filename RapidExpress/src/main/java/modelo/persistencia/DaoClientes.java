/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template 
 */
package modelo.persistencia;
import modelo.clases.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
/**
 *
 * @author sergi
 */
public class DaoClientes {
    public Clientes insertar(Clientes cliente) throws SQLException {
        String sql = "INSERT INTO clientes (numero_identificacion, nombre_completo, telefono, email, direccion, ciudad) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNumeroIdentificacion());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getCiudad());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                    return cliente;
                }
            }
        }
        return null;
    }

    public Clientes obtenerPorIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE numero_identificacion=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearCliente(rs);
            }
        }
        return null;
    }

    public Clientes obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearCliente(rs);
            }
        }
        return null;
    }

    private Clientes mapearCliente(ResultSet rs) throws SQLException {
        Clientes c = new Clientes();
        c.setId(rs.getInt("id"));
        c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setDireccion(rs.getString("direccion"));
        c.setCiudad(rs.getString("ciudad"));
        if (rs.getTimestamp("fecha_creacion") != null) {
            c.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        }
        return c;
    }
}
