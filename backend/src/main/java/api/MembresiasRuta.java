package api;

import com.sun.net.httpserver.HttpExchange;
import dao.MembresiaDAO;

// /api/membresias?socioId=1  → membresías de un socio con su estado
public class MembresiasRuta extends Ruta {
    private final MembresiaDAO dao = new MembresiaDAO();

    @Override
    protected void atender(HttpExchange ex) throws Exception {
        if (!ex.getRequestMethod().equals("GET") || idDeRuta(ex) != null) {
            metodoNoPermitido(ex);
            return;
        }
        int socioId = parametroEntero(ex, "socioId");
        responder(ex, 200, dao.listarPorSocio(socioId).stream().map(Convertir::membresia).toList());
    }
}
