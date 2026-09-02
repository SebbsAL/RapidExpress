/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;
import controlador.ControladorConductores;
import controlador.ControladorAuditoria;
import modelo.clases.Conductores;
import modelo.persistencia.DaoConductores;
import modelo.persistencia.DaoVehiculos;
import modelo.servicios.ServicioConductores;
import modelo.servicios.ServicioVehiculos;
import java.util.List;
/**
 * Vista para gestión de conductores
 * @author Sebastian
 */
public class VistaConductores {
    private ControladorConductores controladorConductores;
    public VistaConductores() {
        DaoConductores daoConductores = new DaoConductores();
        ServicioVehiculos servicioVehiculos = new ServicioVehiculos(new DaoVehiculos());
        ServicioConductores servicioConductores = new ServicioConductores(daoConductores, servicioVehiculos);
        ControladorAuditoria controladorAuditoria = new ControladorAuditoria();
        this.controladorConductores = new ControladorConductores(servicioConductores, controladorAuditoria);
    }
    /**
     * Muestra el menú principal de conductores
     */
    public void mostrarMenuConductores() {
        String[] opciones = {
            "Registrar conductor",
            "Listar conductores",
            "Buscar conductor por identificación",
            "Actualizar datos de conductor",
            "Actualizar estado de conductor",
            "Asignar vehículo a conductor"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTIÓN DE CONDUCTORES", opciones);
            switch (opcion) {
                case 1:
                    registrarConductor();
                    break;
                case 2:
                    listarConductores();
                    break;
                case 3:
                    buscarConductorPorIdentificacion();
                    break;
                case 4:
                    actualizarDatosConductor();
                    break;
                case 5:
                    actualizarEstadoConductor();
                    break;
                case 6:
                    asignarVehiculoAConductor();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opción no válida");
            }
        }
    }
    /**
     * Registra un nuevo conductor
     */
    private void registrarConductor() {
        System.out.println("\n📝 REGISTRO DE CONDUCTOR");
        System.out.println("═══════════════════════════════════════");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Número de identificación: ");
            String nombreCompleto = UtilidadConsola.leerTexto("  Nombre completo: ");
            String tipoLicencia = UtilidadConsola.leerTexto("  Tipo de licencia: ");
            String telefono = UtilidadConsola.leerTexto("  Teléfono: ");
            String email = UtilidadConsola.leerTexto("  Email: ");
            controladorConductores.registrarConductor(
                identificacion, nombreCompleto, tipoLicencia, telefono, email
            );
            UtilidadConsola.mostrarExito("Conductor registrado exitosamente con identificación: " + identificacion);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar conductor: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Lista todos los conductores registrados
     */
    private void listarConductores() {
        System.out.println("\n📋 LISTADO DE CONDUCTORES");
        System.out.println("═══════════════════════════════════════");
        try {
            List<Conductores> conductores = controladorConductores.listarConductores();
            if (conductores.isEmpty()) {
                UtilidadConsola.mostrarInfo("No hay conductores registrados");
            } else {
                System.out.printf("%-15s %-25s %-10s %-12s %-20s %-15s%n",
                    "IDENTIFICACIÓN", "NOMBRE", "LICENCIA", "TELÉFONO", "EMAIL", "ESTADO");
                System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
                for (Conductores c : conductores) {
                    System.out.printf("%-15s %-25s %-10s %-12s %-20s %-15s%n",
                        c.getNumeroIdentificacion(),
                        c.getNombreCompleto(),
                        c.getTipoLicencia(),
                        c.getTelefono(),
                        c.getEmail(),
                        c.getEstado().toString()
                    );
                }
                UtilidadConsola.mostrarInfo("Total de conductores: " + conductores.size());
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar conductores: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Busca un conductor por su identificación
     */
    private void buscarConductorPorIdentificacion() {
        System.out.println("\n🔍 BÚSQUEDA DE CONDUCTOR POR IDENTIFICACIÓN");
        System.out.println("═══════════════════════════════════════");
        try {
            String identificacion = UtilidadConsola.leerTexto(" Ingrese la identificación a buscar: ");
            Conductores conductor = controladorConductores.buscarConductorPorIdentificacion(identificacion);
            if (conductor != null) {
                System.out.println("\n✅ CONDUCTOR ENCONTRADO:");
                System.out.println("  Identificación: " + conductor.getNumeroIdentificacion());
                System.out.println("  Nombre: " + conductor.getNombreCompleto());
                System.out.println("  Tipo de Licencia: " + conductor.getTipoLicencia());
                System.out.println("  Teléfono: " + conductor.getTelefono());
                System.out.println("  Email: " + conductor.getEmail());
                System.out.println("  Estado: " + conductor.getEstado());
            } else {
                UtilidadConsola.mostrarError("No se encontró un conductor con identificación: " + identificacion);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al buscar conductor: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza los datos de un conductor
     */
    private void actualizarDatosConductor() {
        System.out.println("\n✏️  ACTUALIZACIÓN DE DATOS DE CONDUCTOR");
        System.out.println("═══════════════════════════════════════");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Identificación del conductor: ");
            String nombreCompleto = UtilidadConsola.leerTexto("  Nuevo nombre completo: ");
            String tipoLicencia = UtilidadConsola.leerTexto("  Nuevo tipo de licencia: ");
            String telefono = UtilidadConsola.leerTexto("  Nuevo teléfono: ");
            String email = UtilidadConsola.leerTexto("  Nuevo email: ");
            controladorConductores.actualizarDatosConductor(
                identificacion, nombreCompleto, tipoLicencia, telefono, email
            );
            UtilidadConsola.mostrarExito("Datos del conductor actualizados correctamente");
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar datos: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza el estado de un conductor
     */
    private void actualizarEstadoConductor() {
        System.out.println("\n🔄 ACTUALIZACIÓN DE ESTADO DE CONDUCTOR");
        System.out.println("═══════════════════════════════════════");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Identificación del conductor: ");
            System.out.println("\n  Estados disponibles:");
            System.out.println("  [1] ACTIVO");
            System.out.println("  [2] DE_VACACIONES");
            System.out.println("  [3] INACTIVO");
            int opcionEstado = UtilidadConsola.leerEntero("  Seleccione el nuevo estado: ");
            String nuevoEstado = null;
            switch (opcionEstado) {
                case 1:
                    nuevoEstado = "ACTIVO";
                    break;
                case 2:
                    nuevoEstado = "DE_VACACIONES";
                    break;
                case 3:
                    nuevoEstado = "INACTIVO";
                    break;
                default:
                    UtilidadConsola.mostrarError("Estado no válido");
                    return;
            }
            controladorConductores.actualizarEstadoConductor(identificacion, nuevoEstado);
            UtilidadConsola.mostrarExito("Estado del conductor actualizado a: " + nuevoEstado);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar estado: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Asigna un vehículo a un conductor
     */
    private void asignarVehiculoAConductor() {
        System.out.println("\n🚗 ASIGNAR VEHÍCULO A CONDUCTOR");
        System.out.println("═══════════════════════════════════════");
        try {
            String identificacion = UtilidadConsola.leerTexto("  Identificación del conductor: ");
            String placaVehiculo = UtilidadConsola.leerTexto("  Placa del vehículo: ");
            controladorConductores.asignarVehiculoAConductor(identificacion, placaVehiculo);
            UtilidadConsola.mostrarExito("Vehículo " + placaVehiculo + " asignado al conductor " + identificacion);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al asignar vehículo: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}