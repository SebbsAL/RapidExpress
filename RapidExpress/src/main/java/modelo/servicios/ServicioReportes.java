package modelo.servicios;

import modelo.persistencia.DaoReportes;
import java.util.Date;

public class ServicioReportes {

    private final DaoReportes daoReportes;

    public ServicioReportes(DaoReportes daoReportes) {
        this.daoReportes = daoReportes;
    }

    public void obtenerReporteEntregasPorConductor(Date fechaInicio, Date fechaFin) {
        daoReportes.imprimirEntregasPorConductor(fechaInicio, fechaFin);
    }

    public void obtenerHistorialRutasVehiculo(String placa) {
        daoReportes.imprimirHistorialVehiculos(placa);
    }
}
