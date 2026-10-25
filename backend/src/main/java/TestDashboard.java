import dao.DashboardDAO;
import java.sql.SQLException;
import java.time.LocalDate;

// Prueba de los números del panel principal
public class TestDashboard {

    public static void main(String[] args) {
        try {
            System.out.println("Hoy es " + LocalDate.now());
            System.out.println(new DashboardDAO().obtener());
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
