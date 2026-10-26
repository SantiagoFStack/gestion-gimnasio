import dao.SocioDAO;
import modelo.Socio;
import java.sql.SQLException;

// Prueba del CRUD de socios: crea un socio de prueba, lo edita y lo borra.
// Al terminar, la base queda igual que al empezar.
public class TestSocios {

    public static void main(String[] args) {
        SocioDAO dao = new SocioDAO();

        try {
            System.out.println("== Listar ==");
            for (Socio s : dao.listar()) {
                System.out.println(s);
            }

            System.out.println("\n== Crear ==");
            Socio nuevo = new Socio("9990001112", "Prueba", "Test", "prueba@mail.com", "3000000000");
            int id = dao.insertar(nuevo);
            System.out.println("Creado con id " + id + ": " + dao.buscarPorId(id));

            System.out.println("\n== Editar ==");
            nuevo.setTelefono("3111111111");
            dao.actualizar(nuevo);
            System.out.println("Teléfono cambiado: " + dao.buscarPorId(id));

            System.out.println("\n== Buscar \"prueba\" ==");
            for (Socio s : dao.buscar("prueba")) {
                System.out.println(s);
            }

            System.out.println("\n== Crear con DNI repetido (debe fallar) ==");
            try {
                dao.insertar(new Socio("9990001112", "Otro", "Repetido", null, null));
            } catch (SQLException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }

            System.out.println("\n== Eliminar ==");
            dao.eliminar(id);
            System.out.println("Eliminado. ¿Sigue existiendo? " + (dao.buscarPorId(id) != null));

            System.out.println("\n== Eliminar un socio con membresías (debe fallar) ==");
            try {
                dao.eliminar(1);
            } catch (SQLException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
