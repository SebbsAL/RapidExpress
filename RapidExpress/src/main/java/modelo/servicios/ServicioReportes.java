package modelo.servicios;

import modelo.persistencia.DaoReportes;
import java.util.Date;
import java.util.List;

public class ServicioReportes {

    private final DaoReportes daoReportes;

    public ServicioReportes(DaoReportes daoReportes) {
        this.daoReportes = daoReportes;
    }

    public List<String> obtenerReporteEntregasPorConductor(String identificacionConductor, Date fechaInicio, Date fechaFin) {
        return daoReportes.obtenerEntregasPorConductor(identificacionConductor, fechaInicio, fechaFin);
    }

    public List<String> obtenerHistorialRutasVehiculo(String placa) {
        return daoReportes.obtenerHistorialVehiculo(placa);
    }
}
