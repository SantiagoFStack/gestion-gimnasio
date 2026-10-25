package conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    // Por defecto: el MySQL local de XAMPP
    private static String BASE_DATOS = "gimnasio";
    private static String URL = "jdbc:mysql://localhost:3306/" + BASE_DATOS
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            + "&characterEncoding=UTF-8";
    private static String USUARIO = "root";
    private static String CLAVE = "";
    private static boolean EN_LA_NUBE = false;

    // Si existe src/main/resources/conexion.properties, se usa la base en la nube (Aiven).
    // Ese archivo tiene la contraseña y NO se sube a GitHub (ver conexion.properties.example).
    static {
        // Registra el driver de MySQL a mano: la API atiende cada petición en otro hilo
        // y, según cómo se arranque, Java no siempre lo encuentra solo
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Falta mysql-connector-j en el pom.xml");
        }

        try (InputStream in = Conexion.class.getResourceAsStream("/conexion.properties")) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                BASE_DATOS = p.getProperty("baseDatos", BASE_DATOS);
                URL = p.getProperty("url");
                USUARIO = p.getProperty("usuario");
                CLAVE = p.getProperty("clave");
                EN_LA_NUBE = true;
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer conexion.properties, se usa la base local");
        }
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public static String getBaseDatos() {
        return BASE_DATOS;
    }

    public static String getDondeEsta() {
        return EN_LA_NUBE ? "en la nube, Aiven" : "local, XAMPP";
    }
}
