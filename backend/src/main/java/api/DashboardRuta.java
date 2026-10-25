package api;

import com.sun.net.httpserver.HttpExchange;
import dao.DashboardDAO;
import modelo.Dashboard;
import java.util.LinkedHashMap;
import java.util.Map;

// GET /api/dashboard  → los números del panel principal
public class DashboardRuta extends Ruta {
    private final DashboardDAO dao = new DashboardDAO();

    @Override
    protected void atender(HttpExchange ex) throws Exception {
        if (!ex.getRequestMethod().equals("GET")) {
            metodoNoPermitido(ex);
            return;
        }
        Dashboard d = dao.obtener();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("sociosActivos", d.getSociosActivos());
        m.put("membresiasVencidas", d.getMembresiasVencidas());
        m.put("vencenEstaSemana", d.getVencenEstaSemana());
        m.put("ingresosMes", d.getIngresosMes());
        responder(ex, 200, m);
    }
}
