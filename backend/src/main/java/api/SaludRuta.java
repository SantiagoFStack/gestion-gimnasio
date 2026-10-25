package api;

import com.sun.net.httpserver.HttpExchange;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

// GET /api/salud  → {"estado": "ok"} si la API y la base de datos responden.
// Es pública: la usan el despertador (GitHub Actions) y Render para saber si la API vive.
public class SaludRuta extends Ruta {

    @Override
    protected boolean requiereLogin() {
        return false;
    }

    @Override
    protected void atender(HttpExchange ex) throws Exception {
        if (!ex.getRequestMethod().equals("GET")) {
            metodoNoPermitido(ex);
            return;
        }
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement()) {
            st.executeQuery("SELECT 1").close();   // toca la base para que Aiven tampoco se duerma
        }
        responder(ex, 200, Map.of("estado", "ok"));
    }
}
