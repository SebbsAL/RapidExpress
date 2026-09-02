package modelo.servicios;

import modelo.clases.Clientes;
import modelo.persistencia.DaoClientes;

public class ServicioClientes {

    private final DaoClientes daoClientes;

    public ServicioClientes(DaoClientes daoClientes) {
        this.daoClientes = daoClientes;
    }

    public Clientes registrarOObtenerCliente(String identificacion, String nombre, String telefono, String email, String direccion, String ciudad) {
        Clientes existente = daoClientes.obtenerPorIdentificacion(identificacion);
        if (existente != null) {
            return existente;
        }

        Clientes nuevo = new Clientes();
        nuevo.setNumeroIdentificacion(identificacion);
        nuevo.setNombreCompleto(nombre);
        nuevo.setTelefono(telefono);
        nuevo.setEmail(email);
        nuevo.setDireccion(direccion);
        nuevo.setCiudad(ciudad);

        return daoClientes.insertar(nuevo);
    }

    public Clientes obtenerClientePorId(int id) {
        return daoClientes.obtenerPorId(id);
    }
} 
