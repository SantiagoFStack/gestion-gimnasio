package api;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import dao.PagoDAO;
import modelo.Pago;

// POST /api/pagos             → registrar un pago (crea o renueva la membresía)
// GET  /api/pagos?socioId=1   → historial de pagos de un socio
public class PagosRuta extends Ruta {
    private final PagoDAO dao = new PagoDAO();

    @Override
    protected void atender(HttpExchange ex) throws Exception {
        if (idDeRuta(ex) != null) {
            metodoNoPermitido(ex);
            return;
        }
        switch (ex.getRequestMethod()) {
            case "GET" -> {
                int socioId = parametroEntero(ex, "socioId");
                responder(ex, 200, dao.listarPorSocio(socioId).stream().map(Convertir::pago).toList());
            }
            case "POST" -> {
                JsonObject json = leerJson(ex);
                Pago pago = dao.registrar(
                        entero(json, "socioId"),
                        entero(json, "planId"),
                        dinero(json, "monto"),
                        texto(json, "metodo", true));
                responder(ex, 201, Convertir.pago(pago));
            }
            default -> metodoNoPermitido(ex);
        }
    }
}
