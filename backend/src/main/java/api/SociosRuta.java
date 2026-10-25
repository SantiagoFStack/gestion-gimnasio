package api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dao.SocioDAO;
import modelo.Socio;
import java.util.List;
import java.util.Map;

// /api/socios  y  /api/socios/{id}
public class SociosRuta extends Ruta {
    private final SocioDAO dao = new SocioDAO();

    @Override
    protected void atender(HttpExchange ex) throws Exception {
        Integer id = idDeRuta(ex);

        switch (ex.getRequestMethod()) {
            case "GET" -> {
                if (id == null) {   // lista, con buscador opcional ?buscar=ana
                    String buscar = parametros(ex).getOrDefault("buscar", "");
                    List<Socio> lista = buscar.isBlank() ? dao.listar() : dao.buscar(buscar);
                    responder(ex, 200, lista.stream().map(Convertir::socio).toList());
                } else {
                    responder(ex, 200, Convertir.socio(buscar(id)));
                }
            }
            case "POST" -> {
                if (id != null) { metodoNoPermitido(ex); return; }
                Socio s = leerSocio(leerJson(ex));
                int nuevoId = dao.insertar(s);
                responder(ex, 201, Convertir.socio(dao.buscarPorId(nuevoId)));
            }
            case "PUT" -> {
                if (id == null) { metodoNoPermitido(ex); return; }
                buscar(id);   // 404 si no existe
                Socio s = leerSocio(leerJson(ex));
                s.setId(id);
                dao.actualizar(s);
                responder(ex, 200, Convertir.socio(dao.buscarPorId(id)));
            }
            case "DELETE" -> {
                if (id == null) { metodoNoPermitido(ex); return; }
                if (!dao.eliminar(id)) {
                    throw new NoEncontrado("No existe el socio " + id);
                }
                sinContenido(ex);
            }
            default -> metodoNoPermitido(ex);
        }
    }

    private Socio buscar(int id) throws Exception {
        Socio s = dao.buscarPorId(id);
        if (s == null) {
            throw new NoEncontrado("No existe el socio " + id);
        }
        return s;
    }

    // DNI, nombre y apellido obligatorios; email y teléfono opcionales
    private Socio leerSocio(JsonObject json) {
        String email = texto(json, "email", false);
        if (email != null && !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException("El email no es válido");
        }
        return new Socio(
                texto(json, "dni", true),
                texto(json, "nombre", true),
                texto(json, "apellido", true),
                email,
                texto(json, "telefono", false));
    }
}
