/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.view;

/**
 * Menu principal del sistema RapidExpress
 * Punto de entrada para la interfaz de consola
 * @author Sebastian 
 */
public class MenuPrincipal {
    private VistaVehiculos vistaVehiculos;
    private VistaConductores vistaConductores;
    private VistaClientes vistaClientes;
    private VistaPaquetes vistaPaquetes;
    private VistaRutas vistaRutas;
    private VistaReportes vistaReportes;
    public MenuPrincipal() {
        this.vistaVehiculos = new VistaVehiculos();
        this.vistaConductores = new VistaConductores();
        this.vistaClientes = new VistaClientes();
        this.vistaPaquetes = new VistaPaquetes();
        this.vistaRutas = new VistaRutas();
        this.vistaReportes = new VistaReportes();
    }
    /**
     * Muestra el menu principal del sistema
     */
    public void mostrarMenuPrincipal() {
        String[] opciones = {
            "Gestion de Vehiculos",
            "Gestion de Conductores",
            "Gestion de Clientes",
            "Gestion de Paquetes",
            "Gestion de Rutas",
            "Reportes"
        };
        while (true) {
            UtilidadConsola.limpiarConsola();
            mostrarEncabezado("Sistema de Gestion Logistica");
            int opcion = UtilidadConsola.mostrarMenu("MENU PRINCIPAL", opciones);
            switch (opcion) {
                case 1:
                    vistaVehiculos.mostrarMenuVehiculos();
                    break;
                case 2:
                    vistaConductores.mostrarMenuConductores();
                    break;
                case 3:
                    vistaClientes.mostrarMenuClientes();
                    break;
                case 4:
                    vistaPaquetes.mostrarMenuPaquetes();
                    break;
                case 5:
                    vistaRutas.mostrarMenuRutas();
                    break;
                case 6:
                    vistaReportes.mostrarMenuReportes();
                    break;
                case 0:
                    System.out.println("\nGracias por usar Rapid Express.");
                    System.out.println("Hasta pronto.\n");
                    return;
                default:
                    UtilidadConsola.mostrarError("Opcion no valida");
                    UtilidadConsola.pausar();
            }
        }
    }
    /**
     * Metodo main - Punto de entrada del sistema
     */
    public static void main(String[] args) {
        mostrarEncabezado("Iniciando sistema...");
        try {
            MenuPrincipal menu = new MenuPrincipal();
            menu.mostrarMenuPrincipal();
        } catch (java.util.NoSuchElementException | IllegalStateException e) {
            System.out.println("\nEntrada no disponible. Cerrando el sistema.");
            System.out.println("Hasta pronto.\n");
        } catch (Exception e) {
            System.out.println("\n[ERROR] Error critico al iniciar el sistema:");
            System.out.println("   " + e.getMessage());
            System.out.println("\nContacte al administrador del sistema.\n");
        }
    }

    /**
     * Imprime el encabezado con el nombre del sistema y un subtítulo.
     */
    private static void mostrarEncabezado(String subtitulo) {
        System.out.println("\n========================================");
        System.out.println("             RAPID EXPRESS");
        System.out.println("========================================");
        System.out.println(subtitulo);
    }
}