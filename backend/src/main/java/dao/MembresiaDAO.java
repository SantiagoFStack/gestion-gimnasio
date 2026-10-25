package dao;

import conexion.Conexion;
import modelo.Membresia;
import modelo.Plan;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Lógica de membresías (issue #12): fecha de fin y estado ACTIVA / VENCIDA
public class MembresiaDAO {

    private static final String SELECT = "SELECT m.*, p.nombre AS nombre_plan "
            + "FROM membresia m JOIN plan p ON p.id = m.plan_id ";

    // ---------- Crear: la fecha de fin = fecha de inicio + duración del plan ----------
    public Membresia crear(int socioId, int planId, LocalDate fechaInicio) throws SQLException {
        Plan plan = new PlanDAO().buscarPorId(planId);
        if (plan == null) {
            throw new SQLException("No existe el plan " + planId);
        }

        Membresia m = new Membresia();
        m.setSocioId(socioId);
        m.setPlanId(planId);
        m.setNombrePlan(plan.getNombre());
        m.setFechaInicio(fechaInicio);
        m.setFechaFin(fechaInicio.plusDays(plan.getDuracionDias()));

        String sql = "INSERT INTO membresia (socio_id, plan_id, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, socioId);
            ps.setInt(2, planId);
            ps.setDate(3, Date.valueOf(m.getFechaInicio()));
            ps.setDate(4, Date.valueOf(m.getFechaFin()));
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                claves.next();
                m.setId(claves.getInt(1));
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("No existe el socio " + socioId, e);
        }
        return m;
    }

    // ---------- Membresías de un socio, la más reciente primero ----------
    public List<Membresia> listarPorSocio(int socioId) throws SQLException {
        String sql = SELECT + "WHERE m.socio_id = ? ORDER BY m.fecha_fin DESC";
        List<Membresia> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, socioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leerFila(rs));
                }
            }
        }
        return lista;
    }

    // ---------- Última membresía de un socio (null si nunca tuvo) ----------
    public Membresia ultimaDeSocio(int socioId) throws SQLException {
        List<Membresia> lista = listarPorSocio(socioId);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // ---------- Estado de un socio: el de su última membresía ----------
    public String estadoDeSocio(int socioId) throws SQLException {
        Membresia ultima = ultimaDeSocio(socioId);
        return Membresia.calcularEstado(ultima == null ? null : ultima.getFechaFin());
    }

    // ---------- Eliminar (true si la encontró) ----------
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM membresia WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("No se puede eliminar la membresía " + id + ": tiene pagos registrados", e);
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Membresia
    private Membresia leerFila(ResultSet rs) throws SQLException {
        Membresia m = new Membresia();
        m.setId(rs.getInt("id"));
        m.setSocioId(rs.getInt("socio_id"));
        m.setPlanId(rs.getInt("plan_id"));
        m.setNombrePlan(rs.getString("nombre_plan"));
        m.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        m.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        return m;
    }
}
