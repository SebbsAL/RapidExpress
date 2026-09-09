/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template 
 */
package com.rapidexpress.model.dao;
import com.rapidexpress.model.entity.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementación JDBC del acceso a datos de Clientes.
 *
 * @author sergi
 */
public class DaoClientes implements IDaoClientes {
    /** Inserta un cliente y devuelve el objeto con su id generado. */
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

    /** Actualiza los datos de un cliente existente, localizado por su identificación. */
    public boolean actualizar(Clientes cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre_completo=?, telefono=?, email=?, direccion=?, ciudad=? WHERE numero_identificacion=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getCiudad());
            ps.setString(6, cliente.getNumeroIdentificacion());
            return ps.executeUpdate() > 0;
        }
    }

    /** Obtiene todos los clientes registrados. */
    public List<Clientes> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM clientes";
        List<Clientes> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearCliente(rs));
        }
        return lista;
    }

    /** Busca un cliente por su número de identificación. */
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

    /** Busca un cliente por su id interno. */
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
    
    /** Borra un cliente por su identificación; retorna false si no existia. */
    public boolean eliminar(String identificacion)throws SQLException{
        String sql = "DELETE FROM clientes WHERE numero_identificacion = ?";
        try(Connection con = ConexionBD.MySQLConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, identificacion);
            return ps.executeUpdate()>0;
        }
    }
    
    /** Convierte una fila del ResultSet en un objeto Clientes. */
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
