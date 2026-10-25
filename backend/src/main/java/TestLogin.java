import dao.UsuarioDAO;
import modelo.Usuario;
import java.sql.SQLException;

// Prueba del login con el usuario de prueba admin / admin123
public class TestLogin {

    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();

        try {
            probar(dao, "admin", "admin123");      // correcto
            probar(dao, "admin", "1234");          // contraseña mala
            probar(dao, "pepito", "admin123");     // usuario que no existe
            probar(dao, "", "");                   // vacío
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void probar(UsuarioDAO dao, String usuario, String password) throws SQLException {
        Usuario u = dao.login(usuario, password);
        String resultado = (u != null) ? "ENTRA → " + u : "NO entra: usuario o contraseña incorrectos";
        System.out.println("'" + usuario + "' / '" + password + "' → " + resultado);
    }
}
