package api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dao.UsuarioDAO;
import modelo.Usuario;
import java.util.Map;

// POST /api/login  {"usuario": "admin", "password": "admin123"}
// Devuelve el usuario y su token de sesión
public class LoginRuta extends Ruta {
    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected boolean requiereLogin() {
        return false;   // para entrar no se puede exigir haber entrado
    }

    @Override
    protected void atender(HttpExchange ex) throws Exception {
        if (!ex.getRequestMethod().equals("POST")) {
            metodoNoPermitido(ex);
            return;
        }
        JsonObject json = leerJson(ex);
        Usuario u = dao.login(texto(json, "usuario", false), texto(json, "password", false));
        if (u == null) {
            error(ex, 401, "Usuario o contraseña incorrectos");
        } else {
            Map<String, Object> respuesta = Convertir.usuario(u);
            respuesta.put("token", Token.crear(u));   // el front lo envía en cada petición
            responder(ex, 200, respuesta);
        }
    }
}
