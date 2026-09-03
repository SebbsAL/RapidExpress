package config;

import controlador.ControladorAuditoria;
import controlador.ControladorClientes;
import controlador.ControladorConductores;
import controlador.ControladorMantenimientos;
import controlador.ControladorPaquetes;
import controlador.ControladorReportes;
import controlador.ControladorRutas;
import controlador.ControladorVehiculos;
import modelo.persistencia.DaoClientes;
import modelo.persistencia.DaoConductores;
import modelo.persistencia.DaoMantenimientos;
import modelo.persistencia.DaoPaquetes;
import modelo.persistencia.DaoReportes;
import modelo.persistencia.DaoRutas;
import modelo.persistencia.DaoVehiculos;
import modelo.servicios.ServicioAuditoria;
import modelo.servicios.ServicioClientes;
import modelo.servicios.ServicioConductores;
import modelo.servicios.ServicioMantenimientos;
import modelo.servicios.ServicioPaquetes;
import modelo.servicios.ServicioReportes;
import modelo.servicios.ServicioRutas;
import modelo.servicios.ServicioVehiculos;

/**
 * Composition root del sistema: unico lugar donde se construyen e
 * interconectan las capas Dao -&gt; Servicio -&gt; Controlador. Las Vistas ya
 * no dependen de clases concretas de persistencia, solo llaman a los
 * metodos "crearControladorX" de esta fabrica.
 */
public final class Fabrica {

    private Fabrica() {
    }

    private static final DaoVehiculos DAO_VEHICULOS = new DaoVehiculos();
    private static final DaoConductores DAO_CONDUCTORES = new DaoConductores();
    private static final DaoClientes DAO_CLIENTES = new DaoClientes();
    private static final DaoPaquetes DAO_PAQUETES = new DaoPaquetes();
    private static final DaoRutas DAO_RUTAS = new DaoRutas();
    private static final DaoMantenimientos DAO_MANTENIMIENTOS = new DaoMantenimientos();
    private static final DaoReportes DAO_REPORTES = new DaoReportes();

    private static final ServicioAuditoria SERVICIO_AUDITORIA = new ServicioAuditoria();
    private static final ServicioVehiculos SERVICIO_VEHICULOS = new ServicioVehiculos(DAO_VEHICULOS);
    private static final ServicioConductores SERVICIO_CONDUCTORES = new ServicioConductores(DAO_CONDUCTORES, SERVICIO_VEHICULOS);
    private static final ServicioClientes SERVICIO_CLIENTES = new ServicioClientes(DAO_CLIENTES);
    private static final ServicioPaquetes SERVICIO_PAQUETES = new ServicioPaquetes(DAO_PAQUETES, SERVICIO_AUDITORIA, SERVICIO_CLIENTES);
    private static final ServicioRutas SERVICIO_RUTAS = new ServicioRutas(DAO_RUTAS, SERVICIO_VEHICULOS, SERVICIO_CONDUCTORES, DAO_PAQUETES, SERVICIO_AUDITORIA);
    private static final ServicioMantenimientos SERVICIO_MANTENIMIENTOS = new ServicioMantenimientos(DAO_MANTENIMIENTOS, SERVICIO_VEHICULOS);
    private static final ServicioReportes SERVICIO_REPORTES = new ServicioReportes(DAO_REPORTES);

    private static final ControladorAuditoria CONTROLADOR_AUDITORIA = new ControladorAuditoria();

    public static ControladorVehiculos crearControladorVehiculos() {
        return new ControladorVehiculos(SERVICIO_VEHICULOS, CONTROLADOR_AUDITORIA);
    }

    public static ControladorConductores crearControladorConductores() {
        return new ControladorConductores(SERVICIO_CONDUCTORES, CONTROLADOR_AUDITORIA);
    }

    public static ControladorClientes crearControladorClientes() {
        return new ControladorClientes(SERVICIO_CLIENTES);
    }

    public static ControladorPaquetes crearControladorPaquetes() {
        return new ControladorPaquetes(SERVICIO_PAQUETES, CONTROLADOR_AUDITORIA);
    }

    public static ControladorRutas crearControladorRutas() {
        return new ControladorRutas(SERVICIO_RUTAS, CONTROLADOR_AUDITORIA);
    }

    public static ControladorMantenimientos crearControladorMantenimientos() {
        return new ControladorMantenimientos(SERVICIO_MANTENIMIENTOS, CONTROLADOR_AUDITORIA);
    }

    public static ControladorReportes crearControladorReportes() {
        return new ControladorReportes(SERVICIO_REPORTES, CONTROLADOR_AUDITORIA);
    }
}
