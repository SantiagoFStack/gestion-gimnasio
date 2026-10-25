package dao;

import conexion.Conexion;
import modelo.Membresia;
import modelo.Pago;
import modelo.Plan;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Registro de pagos (issue #13). Cada pago crea una membresía nueva.
public class PagoDAO {

    // ---------- Registrar un pago ----------
    // Si el socio aún tiene una membresía activa, la nueva empieza el día en que vence
    // la anterior, para que no pierda días. Si no, empieza hoy.
    public Pago registrar(int socioId, int planId, BigDecimal monto, String metodo) throws SQLException {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0");
        }
        if (!Pago.METODOS.contains(metodo)) {
            throw new IllegalArgumentException("Método no válido: " + metodo + ". Usa " + Pago.METODOS);
        }
        if (new SocioDAO().buscarPorId(socioId) == null) {
            throw new SQLException("No existe el socio " + socioId);
        }
        Plan plan = new PlanDAO().buscarPorId(planId);
        if (plan == null) {
            throw new SQLException("No existe el plan " + planId);
        }

        Membresia ultima = new MembresiaDAO().ultimaDeSocio(socioId);
        LocalDate inicio = LocalDate.now();
        if (ultima != null && Membresia.ACTIVA.equals(ultima.getEstado())) {
            inicio = ultima.getFechaFin();
        }

        Membresia m = new Membresia();
        m.setSocioId(socioId);
        m.setPlanId(planId);
        m.setNombrePlan(plan.getNombre());
        m.setFechaInicio(inicio);
        m.setFechaFin(inicio.plusDays(plan.getDuracionDias()));

        Pago pago = new Pago();
        pago.setFecha(LocalDate.now());
        pago.setMonto(monto);
        pago.setMetodo(metodo);
        pago.setMembresia(m);

        // Membresía y pago se guardan juntos: si falla uno, no se guarda ninguno (transacción)
        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);
            try {
                String sqlMembresia = "INSERT INTO membresia (socio_id, plan_id, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlMembresia, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, socioId);
                    ps.setInt(2, planId);
                    ps.setDate(3, Date.valueOf(m.getFechaInicio()));
                    ps.setDate(4, Date.valueOf(m.getFechaFin()));
                    ps.executeUpdate();
                    try (ResultSet claves = ps.getGeneratedKeys()) {
                        claves.next();
                        m.setId(claves.getInt(1));
                    }
                }

                String sqlPago = "INSERT INTO pago (membresia_id, fecha, monto, metodo) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, m.getId());
                    ps.setDate(2, Date.valueOf(pago.getFecha()));
                    ps.setBigDecimal(3, monto);
                    ps.setString(4, metodo);
                    ps.executeUpdate();
                    try (ResultSet claves = ps.getGeneratedKeys()) {
                        claves.next();
                        pago.setId(claves.getInt(1));
                    }
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
        pago.setMembresiaId(m.getId());
        return pago;
    }

    // ---------- Historial de pagos de un socio, el más reciente primero ----------
    public List<Pago> listarPorSocio(int socioId) throws SQLException {
        String sql = "SELECT pa.id, pa.fecha, pa.monto, pa.metodo, "
                + "m.id AS membresia_id, m.socio_id, m.plan_id, m.fecha_inicio, m.fecha_fin, p.nombre AS nombre_plan "
                + "FROM pago pa "
                + "JOIN membresia m ON m.id = pa.membresia_id "
                + "JOIN plan p ON p.id = m.plan_id "
                + "WHERE m.socio_id = ? ORDER BY pa.fecha DESC, pa.id DESC";
        List<Pago> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, socioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Membresia m = new Membresia();
                    m.setId(rs.getInt("membresia_id"));
                    m.setSocioId(rs.getInt("socio_id"));
                    m.setPlanId(rs.getInt("plan_id"));
                    m.setNombrePlan(rs.getString("nombre_plan"));
                    m.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                    m.setFechaFin(rs.getDate("fecha_fin").toLocalDate());

                    Pago pago = new Pago();
                    pago.setId(rs.getInt("id"));
                    pago.setMembresiaId(m.getId());
                    pago.setFecha(rs.getDate("fecha").toLocalDate());
                    pago.setMonto(rs.getBigDecimal("monto"));
                    pago.setMetodo(rs.getString("metodo"));
                    pago.setMembresia(m);
                    lista.add(pago);
                }
            }
        }
        return lista;
    }

    // ---------- Anular un pago registrado por error: borra el pago y su membresía ----------
    public boolean anular(int pagoId) throws SQLException {
        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);
            try {
                int membresiaId;
                try (PreparedStatement ps = con.prepareStatement("SELECT membresia_id FROM pago WHERE id = ?")) {
                    ps.setInt(1, pagoId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            return false;
                        }
                        membresiaId = rs.getInt(1);
                    }
                }
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM pago WHERE id = ?")) {
                    ps.setInt(1, pagoId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM membresia WHERE id = ?")) {
                    ps.setInt(1, membresiaId);
                    ps.executeUpdate();
                }
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }
}
