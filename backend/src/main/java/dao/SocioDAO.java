package dao;

import conexion.Conexion;
import modelo.Socio;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// CRUD de la tabla socio (issue #10)
public class SocioDAO {

    // ---------- Listar ----------
    public List<Socio> listar() throws SQLException {
        String sql = "SELECT * FROM socio ORDER BY apellido, nombre";
        List<Socio> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(leerFila(rs));
            }
        }
        return lista;
    }

    // ---------- Buscar por nombre, apellido o DNI ----------
    public List<Socio> buscar(String texto) throws SQLException {
        String sql = "SELECT * FROM socio WHERE nombre LIKE ? OR apellido LIKE ? OR dni LIKE ? ORDER BY apellido, nombre";
        String patron = "%" + texto + "%";
        List<Socio> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leerFila(rs));
                }
            }
        }
        return lista;
    }

    // ---------- Buscar uno por id (null si no existe) ----------
    public Socio buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM socio WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? leerFila(rs) : null;
            }
        }
    }

    // ---------- Crear (devuelve el id nuevo) ----------
    public int insertar(Socio s) throws SQLException {
        String sql = "INSERT INTO socio (dni, nombre, apellido, email, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?, ?)";
        if (s.getFechaRegistro() == null) {
            s.setFechaRegistro(LocalDate.now());
        }
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getDni());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getApellido());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getTelefono());
            ps.setDate(6, Date.valueOf(s.getFechaRegistro()));
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                claves.next();
                s.setId(claves.getInt(1));
                return s.getId();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Ya existe un socio con el DNI " + s.getDni(), e);
        }
    }

    // ---------- Editar (true si encontró el socio) ----------
    public boolean actualizar(Socio s) throws SQLException {
        String sql = "UPDATE socio SET dni = ?, nombre = ?, apellido = ?, email = ?, telefono = ? WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getDni());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getApellido());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getTelefono());
            ps.setInt(6, s.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Ya existe otro socio con el DNI " + s.getDni(), e);
        }
    }

    // ---------- Eliminar (true si encontró el socio) ----------
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM socio WHERE id = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("No se puede eliminar el socio " + id + ": tiene membresías registradas", e);
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Socio
    private Socio leerFila(ResultSet rs) throws SQLException {
        Socio s = new Socio();
        s.setId(rs.getInt("id"));
        s.setDni(rs.getString("dni"));
        s.setNombre(rs.getString("nombre"));
        s.setApellido(rs.getString("apellido"));
        s.setEmail(rs.getString("email"));
        s.setTelefono(rs.getString("telefono"));
        s.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        return s;
    }
}
