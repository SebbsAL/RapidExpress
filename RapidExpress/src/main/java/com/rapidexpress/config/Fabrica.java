package com.rapidexpress.config;

import com.rapidexpress.controller.ControladorAuditoria;
import com.rapidexpress.controller.ControladorClientes;
import com.rapidexpress.controller.ControladorConductores;
import com.rapidexpress.controller.ControladorMantenimientos;
import com.rapidexpress.controller.ControladorPaquetes;
import com.rapidexpress.controller.ControladorReportes;
import com.rapidexpress.controller.ControladorRutas;
import com.rapidexpress.controller.ControladorVehiculos;
import com.rapidexpress.model.dao.DaoAuditoria;
import com.rapidexpress.model.dao.DaoClientes;
import com.rapidexpress.model.dao.DaoConductores;
import com.rapidexpress.model.dao.DaoMantenimientos;
import com.rapidexpress.model.dao.DaoPaquetes;
import com.rapidexpress.model.dao.DaoReportes;
import com.rapidexpress.model.dao.DaoRutas;
import com.rapidexpress.model.dao.DaoVehiculos;
import com.rapidexpress.service.ServicioAuditoria;
import com.rapidexpress.service.ServicioClientes;
import com.rapidexpress.service.ServicioConductores;
import com.rapidexpress.service.ServicioMantenimientos;
import com.rapidexpress.service.ServicioPaquetes;
import com.rapidexpress.service.ServicioReportes;
import com.rapidexpress.service.ServicioRutas;
import com.rapidexpress.service.ServicioVehiculos;

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
    private static final DaoAuditoria DAO_AUDITORIA = new DaoAuditoria();

    private static final ServicioAuditoria SERVICIO_AUDITORIA = new ServicioAuditoria(DAO_AUDITORIA);
    private static final ServicioVehiculos SERVICIO_VEHICULOS = new ServicioVehiculos(DAO_VEHICULOS);
    private static final ServicioConductores SERVICIO_CONDUCTORES = new ServicioConductores(DAO_CONDUCTORES, SERVICIO_VEHICULOS);
    private static final ServicioClientes SERVICIO_CLIENTES = new ServicioClientes(DAO_CLIENTES);
    private static final ServicioPaquetes SERVICIO_PAQUETES = new ServicioPaquetes(DAO_PAQUETES, SERVICIO_CLIENTES);
    private static final ServicioRutas SERVICIO_RUTAS = new ServicioRutas(DAO_RUTAS, SERVICIO_VEHICULOS, SERVICIO_CONDUCTORES, DAO_PAQUETES);
    private static final ServicioMantenimientos SERVICIO_MANTENIMIENTOS = new ServicioMantenimientos(DAO_MANTENIMIENTOS, SERVICIO_VEHICULOS);
    private static final ServicioReportes SERVICIO_REPORTES = new ServicioReportes(DAO_REPORTES);

    private static final ControladorAuditoria CONTROLADOR_AUDITORIA = new ControladorAuditoria(SERVICIO_AUDITORIA);

    /** Crea el controlador de vehículos con sus dependencias ya conectadas. */
    public static ControladorVehiculos crearControladorVehiculos() {
        return new ControladorVehiculos(SERVICIO_VEHICULOS, CONTROLADOR_AUDITORIA);
    }

    /** Crea el controlador de conductores con sus dependencias ya conectadas. */
    public static ControladorConductores crearControladorConductores() {
        return new ControladorConductores(SERVICIO_CONDUCTORES, CONTROLADOR_AUDITORIA);
    }

    /** Crea el controlador de clientes con sus dependencias ya conectadas. */
    public static ControladorClientes crearControladorClientes() {
        return new ControladorClientes(SERVICIO_CLIENTES);
    }

    /** Crea el controlador de paquetes con sus dependencias ya conectadas. */
    public static ControladorPaquetes crearControladorPaquetes() {
        return new ControladorPaquetes(SERVICIO_PAQUETES, CONTROLADOR_AUDITORIA);
    }

    /** Crea el controlador de rutas con sus dependencias ya conectadas. */
    public static ControladorRutas crearControladorRutas() {
        return new ControladorRutas(SERVICIO_RUTAS, CONTROLADOR_AUDITORIA);
    }

    /** Crea el controlador de mantenimientos con sus dependencias ya conectadas. */
    public static ControladorMantenimientos crearControladorMantenimientos() {
        return new ControladorMantenimientos(SERVICIO_MANTENIMIENTOS, CONTROLADOR_AUDITORIA);
    }

    /** Crea el controlador de reportes con sus dependencias ya conectadas. */
    public static ControladorReportes crearControladorReportes() {
        return new ControladorReportes(SERVICIO_REPORTES, CONTROLADOR_AUDITORIA);
    }
}
