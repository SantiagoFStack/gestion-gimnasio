import conexion.Conexion;
import dao.ConsultaDAO;
import java.sql.Connection;
import java.sql.SQLException;

public class Testconnection {

    public static void main(String[] args) {
        String tabla = "socio";

        try (Connection con = Conexion.conectar()) {
            System.out.println("Conexión exitosa a: " + Conexion.getBaseDatos());
        } catch (SQLException e) {
            System.out.println("ERROR al conectar: " + e.getMessage());
            return;
        }

        try {
            new ConsultaDAO().imprimirTabla(tabla);
        } catch (SQLException e) {
            System.out.println("ERROR en la consulta: " + e.getMessage());
        }
    }
}
