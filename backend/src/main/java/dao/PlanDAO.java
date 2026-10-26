package dao;

import conexion.Conexion;
import modelo.Plan;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// CRUD de la tabla plan (issue #11)
public class PlanDAO {

    // ---------- Listar ----------
    public List<Plan> listar() throws SQLException {
        String sql = "SELECT * FROM plan ORDER BY duracion_dias";
        List<Plan> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(leerFila(rs));
            }
        }
        return lista;
    }

    // ---------- Buscar uno por id (null si no existe) ----------
    public Plan buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM plan WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? leerFila(rs) : null;
            }
        }
    }

    // ---------- Crear (devuelve el id nuevo) ----------
    public int insertar(Plan p) throws SQLException {
        validar(p);
        String sql = "INSERT INTO plan (nombre, duracion_dias, precio) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getDuracionDias());
            ps.setBigDecimal(3, p.getPrecio());
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                claves.next();
                p.setId(claves.getInt(1));
                return p.getId();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Ya existe un plan llamado " + p.getNombre(), e);
        }
    }

    // ---------- Editar (true si encontró el plan) ----------
    public boolean actualizar(Plan p) throws SQLException {
        validar(p);
        String sql = "UPDATE plan SET nombre = ?, duracion_dias = ?, precio = ? WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getDuracionDias());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Ya existe otro plan llamado " + p.getNombre(), e);
        }
    }

    // ---------- Eliminar (true si encontró el plan) ----------
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM plan WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("No se puede eliminar el plan " + id + ": tiene membresías registradas", e);
        }
    }

    // Nombre, duración y precio son obligatorios (issue #11)
    private void validar(Plan p) {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del plan es obligatorio");
        }
        if (p.getDuracionDias() <= 0) {
            throw new IllegalArgumentException("La duración debe ser de al menos 1 día");
        }
        if (p.getPrecio() == null || p.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0");
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Plan
    private Plan leerFila(ResultSet rs) throws SQLException {
        Plan p = new Plan();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDuracionDias(rs.getInt("duracion_dias"));
        p.setPrecio(rs.getBigDecimal("precio"));
        return p;
    }
}
