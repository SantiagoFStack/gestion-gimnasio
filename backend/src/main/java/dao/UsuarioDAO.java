package dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import conexion.Conexion;
import modelo.Usuario;
import java.sql.*;

// Login del personal (issue #16)
public class UsuarioDAO {

    // Devuelve el usuario si el nombre y la contraseña son correctos, o null si no.
    // La contraseña está guardada cifrada con BCrypt: no se compara el texto,
    // se comprueba que la contraseña escrita genere el mismo cifrado.
    public Usuario login(String usuario, String password) throws SQLException {
        if (usuario == null || usuario.isBlank() || password == null || password.isEmpty()) {
            return null;
        }
        String sql = "SELECT id, usuario, password, rol FROM usuario WHERE usuario = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String cifrada = rs.getString("password");
                if (!BCrypt.verifyer().verify(password.toCharArray(), cifrada).verified) {
                    return null;
                }
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsuario(rs.getString("usuario"));
                u.setRol(rs.getString("rol"));
                return u;
            }
        }
    }
}
