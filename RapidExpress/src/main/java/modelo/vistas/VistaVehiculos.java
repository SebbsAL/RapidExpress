/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;
import controlador.VehiculoController;
import controlador.ControladorAuditoria;
import modelo.clases.Vehiculos;
import java.util.List;
/**
 * Vista para gestión de vehículos
 * @author Sebastian
 */
public class VistaVehiculos {
    private VehiculoController controladorVehiculos;
    public VistaVehiculos() {
        // Necesitamos instanciar las dependencias del controlador
        modelo.persistencia.DaoVehiculos daoVehiculos = new modelo.persistencia.DaoVehiculos();
        modelo.servicios.ServicioVehiculos servicioVehiculos = new modelo.servicios.ServicioVehiculos(daoVehiculos);
        ControladorAuditoria controladorAuditoria = new ControladorAuditoria();
        this.controladorVehiculos = new VehiculoController(servicioVehiculos, controladorAuditoria);
    }
    /**
     * Muestra el menú principal de vehículos
     */
    public void mostrarMenuVehiculos() {
        String[] opciones = {
            "Registrar vehículo",
            "Listar vehículos",
            "Buscar vehículo por placa",
            "Actualizar datos de vehículo",
            "Actualizar estado de vehículo"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTIÓN DE VEHÍCULOS", opciones);
            switch (opcion) {
                case 1:
                    registrarVehiculo();
                    break;
                case 2:
                    listarVehiculos();
                    break;
                case 3:
                    buscarVehiculoPorPlaca();
                    break;
                case 4:
                    actualizarDatosVehiculo();
                    break;
                case 5:
                    actualizarEstadoVehiculo();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opción no válida");
            }
        }
    }
    /**
     * Registra un nuevo vehículo
     */
    private void registrarVehiculo() {
        System.out.println("\n📝 REGISTRO DE VEHÍCULO");
        System.out.println("═══════════════════════════════════════");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa: ");
            String marca = UtilidadConsola.leerTexto("  Marca: ");
            String modelo = UtilidadConsola.leerTexto("  Modelo: ");
            int anioFabricacion = UtilidadConsola.leerEntero("  Año de fabricación: ");
            double capacidadMaxima = UtilidadConsola.leerDouble("  Capacidad máxima (kg): ");
            controladorVehiculos.registrarVehiculo(
                placa, marca, modelo, anioFabricacion, capacidadMaxima
            );
            UtilidadConsola.mostrarExito("Vehículo registrado exitosamente con placa: " + placa);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar vehículo: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Lista todos los vehículos registrados
     */
    private void listarVehiculos() {
        System.out.println("\n📋 LISTADO DE VEHÍCULOS");
        System.out.println("═══════════════════════════════════════");
        try {
            List<Vehiculos> vehiculos = controladorVehiculos.listarVehiculos();
            if (vehiculos.isEmpty()) {
                UtilidadConsola.mostrarInfo("No hay vehículos registrados");
            } else {
                System.out.printf("%-12s %-15s %-15s %-6s %-10s %-15s%n",
                    "PLACA", "MARCA", "MODELO", "AÑO", "CAPACIDAD", "ESTADO");
                System.out.println("────────────────────────────────────────────────────────────");
                for (Vehiculos v : vehiculos) {
                    System.out.printf("%-12s %-15s %-15s %-6d %-10.2f %-15s%n",
                        v.getPlaca(),
                        v.getMarca(),
                        v.getModelo(),
                        v.getAnio_fabricacion(),
                        v.getCapacidad_maxima_kg(),
                        v.getEstado().toString()
                    );
                }
                UtilidadConsola.mostrarInfo("Total de vehículos: " + vehiculos.size());
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar vehículos: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Busca un vehículo por su placa
     */
    private void buscarVehiculoPorPlaca() {
        System.out.println("\n🔍 BÚSQUEDA DE VEHÍCULO POR PLACA");
        System.out.println("═══════════════════════════════════════");
        try {
            String placa = UtilidadConsola.leerTexto(" Ingrese la placa a buscar: ");
            Vehiculos vehiculo = controladorVehiculos.buscarVehiculoPorPlaca(placa);
            if (vehiculo != null) {
                System.out.println("\n✅ VEHÍCULO ENCONTRADO:");
                System.out.println("  Placa: " + vehiculo.getPlaca());
                System.out.println("  Marca: " + vehiculo.getMarca());
                System.out.println("  Modelo: " + vehiculo.getModelo());
                System.out.println("  Año: " + vehiculo.getAnio_fabricacion());
                System.out.println("  Capacidad Máxima: " + vehiculo.getCapacidad_maxima_kg() + " kg");
                System.out.println("  Estado: " + vehiculo.getEstado());
            } else {
                UtilidadConsola.mostrarError("No se encontró un vehículo con placa: " + placa);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al buscar vehículo: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza los datos de un vehículo
     */
    private void actualizarDatosVehiculo() {
        System.out.println("\n✏️  ACTUALIZACIÓN DE DATOS DE VEHÍCULO");
        System.out.println("═══════════════════════════════════════");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa del vehículo a actualizar: ");
            String nuevaMarca = UtilidadConsola.leerTexto("  Nueva marca: ");
            String nuevoModelo = UtilidadConsola.leerTexto("  Nuevo modelo: ");
            int nuevoAnio = UtilidadConsola.leerEntero("  Nuevo año de fabricación: ");
            double nuevaCapacidad = UtilidadConsola.leerDouble("  Nueva capacidad máxima (kg): ");
            controladorVehiculos.actualizarDatosVehiculo(
                placa, nuevaMarca, nuevoModelo, nuevoAnio, nuevaCapacidad
            );
            UtilidadConsola.mostrarExito("Datos del vehículo actualizados correctamente");
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar datos: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Actualiza el estado de un vehículo
     */
    private void actualizarEstadoVehiculo() {
        System.out.println("\n🔄 ACTUALIZACIÓN DE ESTADO DE VEHÍCULO");
        System.out.println("═══════════════════════════════════════");
        try {
            String placa = UtilidadConsola.leerTexto("  Placa del vehículo: ");
            System.out.println("\n  Estados disponibles:");
            System.out.println("  [1] DISPONIBLE");
            System.out.println("  [2] EN_RUTA");
            System.out.println("  [3] EN_MANTENIMIENTO");
            int opcionEstado = UtilidadConsola.leerEntero("  Seleccione el nuevo estado: ");
            String nuevoEstado = null;
            switch (opcionEstado) {
                case 1:
                    nuevoEstado = "DISPONIBLE";
                    break;
                case 2:
                    nuevoEstado = "EN_RUTA";
                    break;
                case 3:
                    nuevoEstado = "EN_MANTENIMIENTO";
                    break;
                default:
                    UtilidadConsola.mostrarError("Estado no válido");
                    return;
            }
            controladorVehiculos.actualizarEstadoVehiculo(placa, nuevoEstado);
            UtilidadConsola.mostrarExito("Estado del vehículo actualizado a: " + nuevoEstado);
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al actualizar estado: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}