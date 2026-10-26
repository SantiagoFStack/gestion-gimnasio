import dao.MembresiaDAO;
import dao.SocioDAO;
import modelo.Membresia;
import modelo.Socio;
import java.sql.SQLException;
import java.time.LocalDate;

// Prueba de la lógica de membresías: fechas de fin y estado ACTIVA / VENCIDA.
// Crea una membresía de prueba y la borra al final: la base queda como estaba.
public class TestMembresias {

    public static void main(String[] args) {
        MembresiaDAO membresias = new MembresiaDAO();
        SocioDAO socios = new SocioDAO();

        try {
            System.out.println("Hoy es " + LocalDate.now());

            System.out.println("\n== Estado de cada socio ==");
            for (Socio s : socios.listar()) {
                System.out.println(s.getNombre() + " " + s.getApellido() + " → " + s.getEstado());
            }

            System.out.println("\n== Membresías de Ana (socio 1) ==");
            for (Membresia m : membresias.listarPorSocio(1)) {
                System.out.println(m);
            }

            System.out.println("\n== Laura (socio 3) paga el plan Mensual hoy ==");
            System.out.println("Antes: " + membresias.estadoDeSocio(3));
            Membresia nueva = membresias.crear(3, 1, LocalDate.now());
            System.out.println("Creada: " + nueva);
            System.out.println("Ahora: " + membresias.estadoDeSocio(3)
                    + " (en la lista de socios: " + socios.buscarPorId(3).getEstado() + ")");

            System.out.println("\n== Se borra la membresía de prueba ==");
            membresias.eliminar(nueva.getId());
            System.out.println("Laura vuelve a: " + membresias.estadoDeSocio(3));

            System.out.println("\n== Membresía con un plan que no existe (debe fallar) ==");
            try {
                membresias.crear(3, 999, LocalDate.now());
            } catch (SQLException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
