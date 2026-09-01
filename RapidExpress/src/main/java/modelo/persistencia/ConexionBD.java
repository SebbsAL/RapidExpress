/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author sergi
 */
public abstract class ConexionBD {
    private static String url= "jdbc:Mysql://***REMOVED-HOST***:3306/rapidexpress_db";
    private static String user = "root";
    private static String password = "***REMOVED-CREDENTIAL***";
    
    public static Connection con = null;
    
    public static Connection MySQLConnection(){
        con = null;
        try {
            con = DriverManager.getConnection(url,user,password);
            
            if(con != null){
                DatabaseMetaData meta = con.getMetaData();
                System.out.println("Base de datos conectada: "+meta.getDriverName());
            }
        } catch (SQLException ex) {
            System.out.println("Error al conectar la BD: "+ex.getMessage());
        }
        return con;
    }
}
