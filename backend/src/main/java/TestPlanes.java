import dao.PlanDAO;
import modelo.Plan;
import java.math.BigDecimal;
import java.sql.SQLException;

// Prueba del CRUD de planes: crea un plan de prueba, lo edita y lo borra.
// Al terminar, la base queda igual que al empezar.
public class TestPlanes {

    public static void main(String[] args) {
        PlanDAO dao = new PlanDAO();

        try {
            System.out.println("== Listar ==");
            for (Plan p : dao.listar()) {
                System.out.println(p);
            }

            System.out.println("\n== Crear ==");
            Plan nuevo = new Plan("Semanal", 7, new BigDecimal("25000"));
            int id = dao.insertar(nuevo);
            System.out.println("Creado con id " + id + ": " + dao.buscarPorId(id));

            System.out.println("\n== Editar ==");
            nuevo.setPrecio(new BigDecimal("30000"));
            dao.actualizar(nuevo);
            System.out.println("Precio cambiado: " + dao.buscarPorId(id));

            System.out.println("\n== Crear con nombre repetido (debe fallar) ==");
            try {
                dao.insertar(new Plan("Mensual", 30, new BigDecimal("1")));
            } catch (SQLException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }

            System.out.println("\n== Crear sin precio (debe fallar) ==");
            try {
                dao.insertar(new Plan("Gratis", 30, BigDecimal.ZERO));
            } catch (IllegalArgumentException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }

            System.out.println("\n== Eliminar ==");
            dao.eliminar(id);
            System.out.println("Eliminado. ¿Sigue existiendo? " + (dao.buscarPorId(id) != null));

            System.out.println("\n== Eliminar un plan con membresías (debe fallar) ==");
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
