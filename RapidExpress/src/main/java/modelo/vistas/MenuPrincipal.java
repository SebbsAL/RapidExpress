/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;
/**
 * Menú principal del sistema RapidExpress
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
     * Muestra el menú principal del sistema
     */
    public void mostrarMenuPrincipal() {
        String[] opciones = {
            "🚗 Gestión de Vehículos",
            "👤 Gestión de Conductores",
            "👥 Gestión de Clientes",
            "📦 Gestión de Paquetes",
            "🚛 Gestión de Rutas",
            "📊 Reportes"
        };
        while (true) {
            UtilidadConsola.limpiarConsola();
            System.out.println("╔═══════════════════════════════════════╗");
            System.out.println("║                                       ║");
            System.out.println("║      📦 RAPID EXPRESS SYSTEM 🚛       ║");
            System.out.println("║    Sistema de Gestión Logística       ║");
            System.out.println("║                                       ║");
            System.out.println("╚═══════════════════════════════════════╝");
            int opcion = UtilidadConsola.mostrarMenu("MENÚ PRINCIPAL", opciones);
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
                    System.out.println("\n═══════════════════════════════════════");
                    System.out.println("  🙋 ¡Gracias por usar Rapid Express!");
                    System.out.println("  👋 Hasta pronto...");
                    System.out.println("═══════════════════════════════════════\n");
                    return;
                default:
                    UtilidadConsola.mostrarError("Opción no válida");
                    UtilidadConsola.pausar();
            }
        }
    }
    /**
     * Método main - Punto de entrada del sistema
     */
    public static void main(String[] args) {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║                                       ║");
        System.out.println("║      📦 RAPID EXPRESS SYSTEM 🚛       ║");
        System.out.println("║         Iniciando sistema...          ║");
        System.out.println("║                                       ║");
        System.out.println("╚═══════════════════════════════════════╝\n");
        try {
            MenuPrincipal menu = new MenuPrincipal();
            menu.mostrarMenuPrincipal();
        } catch (Exception e) {
            System.out.println("\n❌ Error crítico al iniciar el sistema:");
            System.out.println("   " + e.getMessage());
            System.out.println("\nPor favor contacte al administrador del sistema.\n");
        }
    }
}