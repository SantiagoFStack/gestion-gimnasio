package api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dao.UsuarioDAO;
import modelo.Usuario;

// POST /api/login  {"usuario": "admin", "password": "admin123"}
public class LoginRuta extends Ruta {
    private final UsuarioDAO dao = new UsuarioDAO();

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
            responder(ex, 200, Convertir.usuario(u));
        }
    }
}
