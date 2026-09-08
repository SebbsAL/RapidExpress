/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rapidexpress.view;
import com.rapidexpress.config.Fabrica;
import com.rapidexpress.controller.ControladorPaquetes;
import com.rapidexpress.model.entity.Paquetes;
import com.rapidexpress.model.entity.HistorialPaquetes;
import com.rapidexpress.model.entity.ResumenCategoriaPeso;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Vista para gestion de paquetes
 * @author Sebastian
 */
public class VistaPaquetes {
    private ControladorPaquetes controladorPaquetes;
    public VistaPaquetes() {
        // Fabrica ya conecto Dao -> Servicio -> Controlador; solo se pide el controlador.
        this.controladorPaquetes = Fabrica.crearControladorPaquetes();
    }
    /**
     * Muestra el menu principal de paquetes
     */
    public void mostrarMenuPaquetes() {
        String[] opciones = {
            "Registrar nuevo paquete",
            "Buscar paquete por tracking",
            "Consultar trazabilidad de paquete",
            "Listar paquetes en bodega",
            "Consultar paquetes que llevan X dias sin actualizarse",
            "Listar remitentes con mas de 3 envios",
            "Consultar paquetes enviados y recibidos por cliente",
            "Consultar paquetes categorizados por su peso",
            "Obtener paquete mas/menos pesado"
        };
        while (true) {
            int opcion = UtilidadConsola.mostrarMenu("GESTION DE PAQUETES", opciones);
            switch (opcion) {
                case 1:
                    registrarPaquete();
                    break;
                case 2:
                    buscarPaquetePorTracking();
                    break;
                case 3:
                    consultarTrazabilidad();
                    break;
                case 4:
                    listarPaquetesEnBodega();
                    break;
                case 5:
                    consultarPaqueteSinXdiasSinActualizar();
                    break;
                case 6:
                    contarPedidosPorRemitente();
                    break;
                case 7:
                    consultarEnviadosYRecibidos();
                    break;
                case 8:
                    categorizarPaquetesPorPeso();
                    break;
                case 9:
                    obtenerPaqueteMenosYMasPesado();
                    break;
                case 0:
                    return;
                default:
                    UtilidadConsola.mostrarError("Opcion no valida");
            }
        }
    }
    /**
     * Registra un nuevo paquete en el sistema
     */
    private void registrarPaquete() {
        System.out.println("\nREGISTRO DE NUEVO PAQUETE");
        System.out.println("---------------------------------------");
        try {
            String descripcion = UtilidadConsola.leerTextoObligatorio("  Descripcion del contenido: ");
            double peso = UtilidadConsola.leerDouble("  Peso (kg): ");
            String dimensiones = UtilidadConsola.leerTextoObligatorio("  Dimensiones (alto x ancho x largo): ");
            String direccionOrigen = UtilidadConsola.leerTextoObligatorio("  Direccion de origen: ");
            String direccionDestino = UtilidadConsola.leerTextoObligatorio("  Direccion de destino: ");
            System.out.println("\n DATOS DEL REMITENTE:");
            String remitenteIdentificacion = UtilidadConsola.leerTextoObligatorio("    Numero de identificacion: ");
            String remitenteNombre = UtilidadConsola.leerTextoObligatorio("    Nombre completo: ");
            String remitenteTelefono = UtilidadConsola.leerTextoObligatorio("    Telefono: ");
            String remitenteEmail = UtilidadConsola.leerTexto("    Email: ");
            String remitenteDireccion = UtilidadConsola.leerTextoObligatorio("    Direccion: ");
            String remitenteCiudad = UtilidadConsola.leerTextoObligatorio("    Ciudad: ");
            System.out.println("\n DATOS DEL DESTINATARIO:");
            String destinatarioIdentificacion = UtilidadConsola.leerTextoObligatorio("    Numero de identificacion: ");
            String destinatarioNombre = UtilidadConsola.leerTextoObligatorio("    Nombre completo: ");
            String destinatarioTelefono = UtilidadConsola.leerTextoObligatorio("    Telefono: ");
            String destinatarioEmail = UtilidadConsola.leerTexto("    Email: ");
            String destinatarioDireccion = UtilidadConsola.leerTextoObligatorio("    Direccion: ");
            String destinatarioCiudad = UtilidadConsola.leerTextoObligatorio("    Ciudad: ");
            String codigoSeguimiento = controladorPaquetes.registrarPaquete(
                descripcion, peso, dimensiones, direccionOrigen, direccionDestino,
                remitenteIdentificacion, remitenteNombre, remitenteTelefono, remitenteEmail, remitenteDireccion, remitenteCiudad,
                destinatarioIdentificacion, destinatarioNombre, destinatarioTelefono, destinatarioEmail, destinatarioDireccion, destinatarioCiudad
            );
            if (codigoSeguimiento != null) {
                System.out.println("\nPAQUETE REGISTRADO EXITOSAMENTE:");
                System.out.println("  Codigo de seguimiento: " + codigoSeguimiento);
                UtilidadConsola.mostrarExito("Paquete registrado con codigo: " + codigoSeguimiento);
            } else {
                UtilidadConsola.mostrarError("No se pudo registrar el paquete");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al registrar paquete: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Busca un paquete por su codigo de tracking
     */
    private void buscarPaquetePorTracking() {
        System.out.println("\nBUSQUEDA DE PAQUETE POR TRACKING");
        System.out.println("---------------------------------------");
        try {
            String codigoSeguimiento = UtilidadConsola.leerTextoObligatorio("  Codigo de seguimiento: ");
            Paquetes paquete = controladorPaquetes.buscarPaquetePorTracking(codigoSeguimiento);
            if (paquete != null) {
                System.out.println("\nPAQUETE ENCONTRADO:");
                System.out.println("  Codigo de seguimiento: " + paquete.getCodigoSeguimiento());
                System.out.println("  Descripcion: " + paquete.getDescripcionContenido());
                System.out.println("  Peso: " + paquete.getPeso() + " kg");
                System.out.println("  Dimensiones: " + paquete.getDimensiones());
                System.out.println("  Direccion origen: " + paquete.getDireccionOrigen());
                System.out.println("  Direccion destino: " + paquete.getDireccionDestino());
                System.out.println("  Estado: " + paquete.getEstado());
                System.out.println("\n REMITENTE:");
                System.out.println("    Nombre: " + paquete.getRemitente().getNombreCompleto());
                System.out.println("    Identificacion: " + paquete.getRemitente().getNumeroIdentificacion());
                System.out.println("    Telefono: " + paquete.getRemitente().getTelefono());
                System.out.println("    Email: " + paquete.getRemitente().getEmail());
                System.out.println("\n DESTINATARIO:");
                System.out.println("    Nombre: " + paquete.getDestinatario().getNombreCompleto());
                System.out.println("    Identificacion: " + paquete.getDestinatario().getNumeroIdentificacion());
                System.out.println("    Telefono: " + paquete.getDestinatario().getTelefono());
                System.out.println("    Email: " + paquete.getDestinatario().getEmail());
                UtilidadConsola.mostrarExito("Paquete encontrado exitosamente");
            } else {
                UtilidadConsola.mostrarError("No se encontro un paquete con el codigo: " + codigoSeguimiento);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al buscar paquete: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Consulta la trazabilidad completa de un paquete
     */
    private void consultarTrazabilidad() {
        System.out.println("\nCONSULTAR TRAZABILIDAD DE PAQUETE");
        System.out.println("---------------------------------------");
        try {
            String codigoSeguimiento = UtilidadConsola.leerTextoObligatorio("  Codigo de seguimiento: ");
            List<HistorialPaquetes> historial = controladorPaquetes.consultarTrazabilidadPaquete(codigoSeguimiento);
            if (historial != null && !historial.isEmpty()) {
                System.out.println("\nTRAZABILIDAD DEL PAQUETE: " + codigoSeguimiento);
                System.out.println("---------------------------------------");
                for (HistorialPaquetes evento : historial) {
                    System.out.println("\n  Fecha: " + evento.getFechaRegistro());
                    System.out.println("   Evento: " + evento.getDescripcionEvento());
                    System.out.println("   Estado: " + evento.getEstado());
                    System.out.println("   Ubicacion: " + evento.getUbicacion());
                    System.out.println("---------------------------------------");
                }
                UtilidadConsola.mostrarExito("Trazabilidad consultada exitosamente");
            } else {
                UtilidadConsola.mostrarError("No hay historial registrado para el paquete: " + codigoSeguimiento);
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al consultar trazabilidad: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    /**
     * Lista todos los paquetes que estan actualmente en bodega
     */
    private void listarPaquetesEnBodega() {
        System.out.println("\nPAQUETES EN BODEGA");
        System.out.println("---------------------------------------");
        try {
            List<Paquetes> paquetes = controladorPaquetes.listarPaquetesEnBodega();
            if (paquetes != null && !paquetes.isEmpty()) {
                System.out.println("\n Total de paquetes en bodega: " + paquetes.size());
                System.out.println("---------------------------------------");
                for (Paquetes paquete : paquetes) {
                    System.out.println("\n  Codigo: " + paquete.getCodigoSeguimiento());
                    System.out.println("   Descripcion: " + paquete.getDescripcionContenido());
                    System.out.println("    Peso: " + paquete.getPeso() + " kg");
                    System.out.println("   Dimensiones: " + paquete.getDimensiones());
                    System.out.println("   Origen: " + paquete.getDireccionOrigen());
                    System.out.println("   Destino: " + paquete.getDireccionDestino());
                    System.out.println("   Remitente: " + paquete.getRemitente().getNombreCompleto());
                    System.out.println("   Destinatario: " + paquete.getDestinatario().getNombreCompleto());
                    System.out.println("---------------------------------------");
                }
                UtilidadConsola.mostrarExito("Listado completado: " + paquetes.size() + " paquetes");
            } else {
                UtilidadConsola.mostrarInfo("No hay paquetes en bodega actualmente");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar paquetes: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    
    /**
     * Muestra el paquete mas pesado y el mas liviano de los que estan en bodega
     */
    private void obtenerPaqueteMenosYMasPesado(){
        System.out.println("Consultar paquete mas y menos pesado de la bodega");
        System.out.println("-------------------------------------------------");
        try {
            Paquetes paqueteMasPesado = controladorPaquetes.obtenerPaqueteMasPesado();
            Paquetes paqueteMenosPesado = controladorPaquetes.obtenerPaqueteMenosPesado();
            
            if (paqueteMasPesado == null || paqueteMenosPesado == null) {
                UtilidadConsola.mostrarError("No se pudo obtener alguno de los 2 paquetes");
            }else{
                System.out.println("--------------------------------------------------------------------------------------------------");
                System.out.println("Paquete mas pesado: \n CODIGO: "+paqueteMasPesado.getCodigoSeguimiento()+"\n DESCRIPCION: "+paqueteMasPesado.getDescripcionContenido()+"\nPESO: "+paqueteMasPesado.getPesoKg());
                System.out.println("--------------------------------------------------------------------------------------------------");
                System.out.println("\n--------------------------------------------------------------------------------------------------");
                System.out.println("Paquete menos pesado: \n CODIGO: "+paqueteMenosPesado.getCodigoSeguimiento()+"\n DESCRIPCION: "+paqueteMenosPesado.getDescripcionContenido()+"\nPESO: "+paqueteMenosPesado.getPesoKg());
                System.out.println("--------------------------------------------------------------------------------------------------");                
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al consultar paquetes: "+ e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    
    

    /**
     * Muestra los paquetes que llevan N o mas dias sin actualizarse
     */
    private void consultarPaqueteSinXdiasSinActualizar() {
        System.out.println("Consultar paquetes que llevan X dias sin actualizarse");
        System.out.println("-------------------------------------------------------");
        try {
            int dias = UtilidadConsola.leerEntero("Ingrese cuantos dias de no actualizarse quiere consultar: ");
            List<Paquetes> paquetesMora= controladorPaquetes.buscarPorDiasMora(dias);
            if (paquetesMora != null && !paquetesMora.isEmpty()) {
                System.out.println("Paquetes con "+dias+" de no recibir actualizacion");
                System.out.println("-----------------------------------------------------");
                for (Paquetes paquetes : paquetesMora) {
                    System.out.println("\n--------------------------------------------------------------");
                    System.out.println("Codigo: "+paquetes.getCodigoSeguimiento());
                    System.out.println("Estado: "+paquetes.getEstado());
                    System.out.println("Ultima actualizacion: "+paquetes.getFechaActualizacion());
                    System.out.println("--------------------------------------------------------------");
                }
            }else{
                System.out.println("No hay ningun paquete sin recibir actualizacion hace "+ dias+" dias");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al listar paquetes: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    
    /**
     * Muestra los remitentes que han enviado 3 o mas paquetes, con su total
     */
    private void contarPedidosPorRemitente(){
        System.out.println("----------------------------------------------------------------");
        System.out.println("Lista de remitentes con mas de 3 envios");
        try {
            Map<String, Integer> mapa = controladorPaquetes.contarPedidosPorRemitente();
            if (mapa != null && !mapa.isEmpty()) {
                mapa.forEach((nombre,pedidos)->{
                    System.out.println("\n----------------------------------------");
                    System.out.println("Nombre: "+nombre);
                    System.out.println("Cantidad de pedidos: "+pedidos);
                });
            }else{
                System.out.println("No se encontraron remitentes");
            }
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al buscar remitentes: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }

    /**
     * Muestra cuantos paquetes ha enviado y recibido un cliente, y el total
     */
    private void consultarEnviadosYRecibidos() {
        System.out.println("\nCONSULTAR PAQUETES ENVIADOS Y RECIBIDOS POR CLIENTE");
        System.out.println("---------------------------------------");
        try {
            String identificacion = UtilidadConsola.leerTextoObligatorio("  Numero de identificacion del cliente: ");
            int enviados = controladorPaquetes.contarEnviados(identificacion);
            int recibidos = controladorPaquetes.contarRecibidos(identificacion);
            int total = enviados + recibidos;
            System.out.println("\n  Enviados: " + enviados);
            System.out.println("  Recibidos: " + recibidos);
            System.out.println("  Total: " + total);
            UtilidadConsola.mostrarExito("Consulta realizada exitosamente");
        } catch (Exception e) {
            UtilidadConsola.mostrarError("Error al consultar: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
    
    /**
     * Muestra los paquetes en bodega agrupados por rango de peso, con la
     * cantidad y el peso total de cada categoria
     */
    private void categorizarPaquetesPorPeso(){
        System.out.println("\nPAQUETES EN BODEGA POR CATEGORIA DE PESO");
        System.out.println("---------------------------------------");
        try{
            List<ResumenCategoriaPeso> categorizarPeso = controladorPaquetes.categorizarPaquetesPorPeso();
            System.out.printf("%-12s %-10s %-10s%n", "CATEGORIA", "CANTIDAD", "PESO TOTAL");
            System.out.println("");
            for (ResumenCategoriaPeso r : categorizarPeso) {
                System.out.printf("%-12s %-10d %-10.2f kg%n",
                r.getCategoria(), r.getCantidad(), r.getPesoTotal());
            }
        }catch(Exception e){
            UtilidadConsola.mostrarError("Error al categorizar: " + e.getMessage());
        }
        UtilidadConsola.pausar();
    }
}