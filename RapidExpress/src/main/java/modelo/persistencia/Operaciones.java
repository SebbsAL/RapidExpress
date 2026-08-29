/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergi
 */
public class Operaciones {
    
    public static Connection con;
    public static Statement stmt = null;
    public static ResultSet rs = null;
    
    public static Connection setConnection(Connection connection){
        Operaciones.con=connection;
        return connection;
    }
    public static Connection getConnection(){
        return con;
    }
    
    // IMPORTANTE: cierre de conexion 
    public static void closeConnection(Connection con){
        if(con != null){
            try{
                con.close();
            }catch(SQLException ex){
                Logger.getLogger(Operaciones.class.getName()).log(Level.SEVERE,null,ex);                
            }
        }
    }
    
    // Metodo generico para ejecutar consultas de lectura(SELECT)
    public static ResultSet consultar_BD(PreparedStatement sentencia){
        try {
            rs = sentencia.executeQuery();
        } catch (SQLException|RuntimeException sqlex) {
            System.out.println("Error al consultar: "+sqlex);
            return null;
        }
        return rs;
    }
    
    //GENERICO (INSERT, UPDATE, DELETE) executeUpdate()
    public static int insertar_actualizar_borrar_BD(PreparedStatement sentencia){
        int filas; // guarda el numero de filas afectadas
        try {
            filas = sentencia.executeUpdate();
        } catch (SQLException|RuntimeException sqlex) {
            System.out.println("ERROR al modificar: "+ sqlex);
            return 0;
        }
        return filas;
    }
    
    //COMMIT 
    public static boolean SetAutoCommit(boolean parametro){
        try {
            con.setAutoCommit(parametro);
        } catch (SQLException sqlex) {
            return false;
        }
        return true;
    }
    
    public static void cerrarConexion(){
        closeConnection(con);
    }
    
    public static boolean commitBD(){
        try {
            con.commit();
            return  true;
        } catch (SQLException sqlex){
            return false;
        }
    }
        
    public static boolean rollbackBD(){
        try {
            con.rollback();
            return true;
        } catch (SQLException sqlex) {
            return false;
        }
    }  
}
