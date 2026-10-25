package dao;

import conexion.Conexion;
import modelo.Dashboard;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

// Números del panel principal (issue #17)
public class DashboardDAO {

    // La fecha de "hoy" la pone Java y no la base de datos: la base en la nube
    // está en otra zona horaria y por la noche ya sería mañana para ella.
    public Dashboard obtener() throws SQLException {
        LocalDate hoy = LocalDate.now();
        Dashboard d = new Dashboard();

        // Estado de cada socio según la fecha de fin de su última membresía (vence)
        String sqlSocios = "SELECT "
                + "SUM(vence >= ?) AS activos, "
                + "SUM(vence IS NULL OR vence < ?) AS vencidos, "
                + "SUM(vence BETWEEN ? AND ?) AS esta_semana "
                + "FROM (SELECT s.id, MAX(m.fecha_fin) AS vence "
                + "      FROM socio s LEFT JOIN membresia m ON m.socio_id = s.id "
                + "      GROUP BY s.id) AS ultima";

        // Suma de los pagos desde el día 1 de este mes hasta el día 1 del siguiente
        String sqlIngresos = "SELECT COALESCE(SUM(monto), 0) FROM pago WHERE fecha >= ? AND fecha < ?";

        try (Connection con = Conexion.conectar()) {
            try (PreparedStatement ps = con.prepareStatement(sqlSocios)) {
                ps.setDate(1, Date.valueOf(hoy));
                ps.setDate(2, Date.valueOf(hoy));
                ps.setDate(3, Date.valueOf(hoy));
                ps.setDate(4, Date.valueOf(hoy.plusDays(7)));
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    d.setSociosActivos(rs.getInt("activos"));
                    d.setMembresiasVencidas(rs.getInt("vencidos"));
                    d.setVencenEstaSemana(rs.getInt("esta_semana"));
                }
            }

            LocalDate inicioMes = hoy.withDayOfMonth(1);
            try (PreparedStatement ps = con.prepareStatement(sqlIngresos)) {
                ps.setDate(1, Date.valueOf(inicioMes));
                ps.setDate(2, Date.valueOf(inicioMes.plusMonths(1)));
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    BigDecimal total = rs.getBigDecimal(1);
                    d.setIngresosMes(total);
                }
            }
        }
        return d;
    }
}
