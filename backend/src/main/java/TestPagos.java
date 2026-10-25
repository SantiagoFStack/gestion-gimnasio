import dao.MembresiaDAO;
import dao.PagoDAO;
import modelo.Pago;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

// Prueba del registro de pagos. Registra dos pagos de prueba y los anula al final:
// la base queda como estaba.
public class TestPagos {

    public static void main(String[] args) {
        PagoDAO pagos = new PagoDAO();
        MembresiaDAO membresias = new MembresiaDAO();

        try {
            System.out.println("Hoy es " + LocalDate.now());

            System.out.println("\n== Ana (activa hasta " + membresias.ultimaDeSocio(1).getFechaFin()
                    + ") paga otro mes: debe empezar cuando vence el anterior ==");
            Pago pagoAna = pagos.registrar(1, 1, new BigDecimal("80000"), "TARJETA");
            System.out.println(pagoAna);

            System.out.println("\n== Laura (vencida) paga el Trimestral: debe empezar hoy ==");
            System.out.println("Antes: " + membresias.estadoDeSocio(3));
            Pago pagoLaura = pagos.registrar(3, 2, new BigDecimal("220000"), "EFECTIVO");
            System.out.println(pagoLaura);
            System.out.println("Ahora: " + membresias.estadoDeSocio(3));

            System.out.println("\n== Historial de pagos de Ana ==");
            for (Pago p : pagos.listarPorSocio(1)) {
                System.out.println(p);
            }

            System.out.println("\n== Pagos que deben fallar ==");
            try {
                pagos.registrar(1, 1, BigDecimal.ZERO, "EFECTIVO");
            } catch (IllegalArgumentException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }
            try {
                pagos.registrar(1, 1, new BigDecimal("80000"), "BITCOIN");
            } catch (IllegalArgumentException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }
            try {
                pagos.registrar(999, 1, new BigDecimal("80000"), "EFECTIVO");
            } catch (SQLException e) {
                System.out.println("Error esperado: " + e.getMessage());
            }

            System.out.println("\n== Se anulan los pagos de prueba ==");
            pagos.anular(pagoAna.getId());
            pagos.anular(pagoLaura.getId());
            System.out.println("Ana vuelve a vencer el " + membresias.ultimaDeSocio(1).getFechaFin()
                    + " y Laura vuelve a: " + membresias.estadoDeSocio(3));

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
