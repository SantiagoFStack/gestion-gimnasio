package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String BASE_DATOS = "gimnasio";
    private static final String URL = "jdbc:mysql://localhost:3306/" + BASE_DATOS
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            + "&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public static String getBaseDatos() {
        return BASE_DATOS;
    }
}
