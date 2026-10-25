package api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dao.PlanDAO;
import modelo.Plan;

// /api/planes  y  /api/planes/{id}
public class PlanesRuta extends Ruta {
    private final PlanDAO dao = new PlanDAO();

    @Override
    protected void atender(HttpExchange ex) throws Exception {
        Integer id = idDeRuta(ex);

        switch (ex.getRequestMethod()) {
            case "GET" -> {
                if (id == null) {
                    responder(ex, 200, dao.listar().stream().map(Convertir::plan).toList());
                } else {
                    responder(ex, 200, Convertir.plan(buscar(id)));
                }
            }
            case "POST" -> {
                if (id != null) { metodoNoPermitido(ex); return; }
                Plan p = leerPlan(leerJson(ex));
                dao.insertar(p);
                responder(ex, 201, Convertir.plan(p));
            }
            case "PUT" -> {
                if (id == null) { metodoNoPermitido(ex); return; }
                buscar(id);   // 404 si no existe
                Plan p = leerPlan(leerJson(ex));
                p.setId(id);
                dao.actualizar(p);
                responder(ex, 200, Convertir.plan(dao.buscarPorId(id)));
            }
            case "DELETE" -> {
                if (id == null) { metodoNoPermitido(ex); return; }
                if (!dao.eliminar(id)) {
                    throw new NoEncontrado("No existe el plan " + id);
                }
                sinContenido(ex);
            }
            default -> metodoNoPermitido(ex);
        }
    }

    private Plan buscar(int id) throws Exception {
        Plan p = dao.buscarPorId(id);
        if (p == null) {
            throw new NoEncontrado("No existe el plan " + id);
        }
        return p;
    }

    private Plan leerPlan(JsonObject json) {
        return new Plan(texto(json, "nombre", true), entero(json, "duracionDias"), dinero(json, "precio"));
    }
}
