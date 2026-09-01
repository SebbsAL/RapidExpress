/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;
import java.util.Scanner;
/**
 * Utilidad para manejo de entrada por consola
 * @author RapidExpress
 */
public class UtilidadConsola {
    private static Scanner scanner = new Scanner(System.in);
    /**
     * Lee una línea de texto desde la consola
     */
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
    /**
     * Lee un número entero desde la consola
     */
    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Por favor ingrese un número entero válido.");
            }
        }
    }
    /**
     * Lee un número decimal desde la consola
     */
    public static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Por favor ingrese un número decimal válido.");
            }
        }
    }
    /**
     * Muestra un menú y retorna la opción seleccionada
     */
    public static int mostrarMenu(String titulo, String[] opciones) {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  " + titulo);
        System.out.println("═══════════════════════════════════════");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println("  [" + (i + 1) + "] " + opciones[i]);
        }
        System.out.println("  [0] Salir / Volver");
        System.out.println("═══════════════════════════════════════");
        return leerEntero("  Seleccione una opción: ");
    }
    /**
     * Muestra un mensaje de éxito
     */
    public static void mostrarExito(String mensaje) {
        System.out.println("\n✅ " + mensaje);
        System.out.println("═══════════════════════════════════════\n");
    }
    /**
     * Muestra un mensaje de error amigable
     */
    public static void mostrarError(String mensaje) {
        System.out.println("\n❌ Error: " + mensaje);
        System.out.println("═══════════════════════════════════════\n");
    }
    /**
     * Muestra un mensaje informativo
     */
    public static void mostrarInfo(String mensaje) {
        System.out.println("\nℹ️  " + mensaje);
        System.out.println("═══════════════════════════════════════\n");
    }
    /**
     * Limpia la consola (simulado con saltos de línea)
     */
    public static void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    /**
     * Pausa la ejecución hasta que el usuario presione Enter
     */
    public static void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}