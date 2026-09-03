/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;
import java.util.Scanner;
/**
 * Utilidad para manejo de entrada por consola
 * @author Sebastian 
 */
public class UtilidadConsola {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String LINEA = "----------------------------------------";
    /**
     * Lee una linea de texto desde la consola
     */
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
    /**
     * Lee un numero entero desde la consola
     */
    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Ingrese un numero entero valido.");
            }
        }
    }
    /**
     * Lee un numero decimal desde la consola
     */
    public static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Ingrese un numero decimal valido.");
            }
        }
    }
    /**
     * Muestra un menu y retorna la opcion seleccionada
     */
    public static int mostrarMenu(String titulo, String[] opciones) {
        System.out.println("\n" + LINEA);
        System.out.println("  " + titulo);
        System.out.println(LINEA);
        for (int i = 0; i < opciones.length; i++) {
            System.out.println("  " + (i + 1) + ") " + opciones[i]);
        }
        System.out.println("  0) Salir / Volver");
        System.out.println(LINEA);
        return leerEntero("Seleccione una opcion: ");
    }
    /**
     * Muestra un mensaje de exito
     */
    public static void mostrarExito(String mensaje) {
        System.out.println("\n[OK] " + mensaje);
        System.out.println(LINEA + "\n");
    }
    /**
     * Muestra un mensaje de error amigable
     */
    public static void mostrarError(String mensaje) {
        System.out.println("\n[ERROR] " + mensaje);
        System.out.println(LINEA + "\n");
    }
    /**
     * Muestra un mensaje informativo
     */
    public static void mostrarInfo(String mensaje) {
        System.out.println("\n[INFO] " + mensaje);
        System.out.println(LINEA + "\n");
    }
    /**
     * Limpia la consola (simulado con saltos de linea)
     */
    public static void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    /**
     * Pausa la ejecucion hasta que el usuario presione Enter
     */
    public static void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}
