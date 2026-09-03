/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Conexión a la base de datos. La URL, usuario y contraseña se leen del
 * archivo "db.properties" en el directorio de trabajo (mismo nivel que 
 * pom.xml); si una clave no está presente se usa un valor por defecto para
 * desarrollo local. Así no quedan credenciales en el código fuente.
 * "db.properties" nunca debe subirse al repositorio (ver .gitignore);
 * usa db.properties.example como plantilla.
 *
 * @author sergi
 */
public abstract class ConexionBD {
    private static final Properties PROPIEDADES = cargarPropiedades();

    private static String url = PROPIEDADES.getProperty("DB_URL");
    private static String user = PROPIEDADES.getProperty("DB_USER");
    private static String password = PROPIEDADES.getProperty("DB_PASSWORD");

    private static Properties cargarPropiedades() {
        Properties propiedades = new Properties();
        Path ruta = Path.of("db.properties");
        if (!Files.exists(ruta)) {
            return propiedades;
        }
        try (FileInputStream entrada = new FileInputStream(ruta.toFile())) {
            propiedades.load(entrada);
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo db.properties: " + e.getMessage());
        }
        return propiedades;
    }

    public static Connection MySQLConnection() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        DatabaseMetaData meta = con.getMetaData();
        System.out.println("Base de datos conectada: " + meta.getDriverName());
        return con;
    }
}
