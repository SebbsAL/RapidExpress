/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;
import modelo.clases.Conductores;
import modelo.clases.EstadoConductor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author sergi
 */
public class DaoConductores {
     public void insertar(Conductores conductor) {
        String sql = "INSERT INTO conductores (numero_identificacion, nombre_completo, tipo_licencia, telefono, email, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, conductor.getNumeroIdentificacion());
            ps.setString(2, conductor.getNombreCompleto());
            ps.setString(3, conductor.getTipoLicencia());
            ps.setString(4, conductor.getTelefono());
            ps.setString(5, conductor.getEmail());
            ps.setString(6, conductor.getEstado().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar conductor: " + e.getMessage());
        }
    }

    public void actualizar(Conductores conductor) {
        String sql = "UPDATE conductores SET nombre_completo=?, tipo_licencia=?, telefono=?, email=? WHERE numero_identificacion=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, conductor.getNombreCompleto());
            ps.setString(2, conductor.getTipoLicencia());
            ps.setString(3, conductor.getTelefono());
            ps.setString(4, conductor.getEmail());
            ps.setString(5, conductor.getNumeroIdentificacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar conductor: " + e.getMessage());
        }
    }

    public void actualizarEstado(String identificacion, EstadoConductor nuevoEstado) {
        String sql = "UPDATE conductores SET estado=? WHERE numero_identificacion=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, identificacion);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del conductor: " + e.getMessage());
        }
    }

    public Conductores obtenerPorIdentificacion(String identificacion) {
        String sql = "SELECT * FROM conductores WHERE numero_identificacion=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearConductor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener conductor: " + e.getMessage());
        }
        return null;
    }

    public Conductores obtenerPorId(int id) {
        String sql = "SELECT * FROM conductores WHERE id=?";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearConductor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener conductor: " + e.getMessage());
        }
        return null;
    }

    public List<Conductores> obtenerTodos() {
        String sql = "SELECT * FROM conductores";
        List<Conductores> lista = new ArrayList<>();
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearConductor(rs));
        } catch (SQLException e) {
            System.err.println("Error al obtener conductores: " + e.getMessage());
        }
        return lista;
    }

    private Conductores mapearConductor(ResultSet rs) throws SQLException {
        Conductores c = new Conductores();
        c.setId(rs.getInt("id"));
        c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setTipoLicencia(rs.getString("tipo_licencia"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setEstado(EstadoConductor.valueOf(rs.getString("estado")));
        if (rs.getTimestamp("fecha_creacion") != null) {
            c.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        }
        if (rs.getTimestamp("fecha_actualizacion") != null) {
            c.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion").toLocalDateTime());
        }
        return c;
    }

    public boolean tieneAsignacionActiva(String identificacionConductor) {
        String sql = "SELECT count(*) AS total FROM asignaciones_vehiculo_conductor a JOIN conductores c ON a.conductor_id = c.id WHERE c.numero_identificacion=? AND a.activo=1";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, identificacionConductor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error validando asignación: " + e.getMessage());
        }
        return false;
    }

    public void registrarAsignacion(int idVehiculo, int idConductor) {
        String sql = "INSERT INTO asignaciones_vehiculo_conductor (vehiculo_id, conductor_id, activo) VALUES (?, ?, 1)";
        try (Connection con = ConexionBD.MySQLConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVehiculo);
            ps.setInt(2, idConductor);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar asignación: " + e.getMessage());
        }
    }
}
