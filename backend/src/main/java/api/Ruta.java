package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLRecoverableException;
import java.util.HashMap;
import java.util.Map;

// Base de todas las rutas de la API: CORS, leer el JSON que llega,
// responder JSON y convertir los errores en códigos HTTP (400, 404, 500...).
public abstract class Ruta implements HttpHandler {

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    // Cada ruta escribe aquí lo que hace con GET, POST, PUT o DELETE
    protected abstract void atender(HttpExchange ex) throws Exception;

    // Por defecto todas las rutas exigen haber iniciado sesión (token).
    // Las públicas (login y salud) lo cambian a false.
    protected boolean requiereLogin() {
        return true;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        // CORS: deja que el navegador llame a la API desde otra dirección
        // (Live Server en el puerto 5500 o GitHub Pages)
        Headers h = ex.getResponseHeaders();
        h.add("Access-Control-Allow-Origin", "*");
        h.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        h.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        h.add("Access-Control-Allow-Private-Network", "true");

        try {
            if (ex.getRequestMethod().equals("OPTIONS")) {   // pregunta previa del navegador
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (requiereLogin() && !Token.esValido(tokenDe(ex))) {
                error(ex, 401, "Tienes que iniciar sesión");
                return;
            }
            atender(ex);
        } catch (NoEncontrado e) {
            error(ex, 404, e.getMessage());
        } catch (IllegalArgumentException e) {
            error(ex, 400, e.getMessage());
        } catch (SQLRecoverableException | SQLNonTransientConnectionException e) {
            error(ex, 503, "No se pudo conectar a la base de datos");
        } catch (SQLException e) {
            error(ex, 400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            error(ex, 500, "Error interno del servidor");
        } finally {
            ex.close();
        }
    }

    // ---------- Responder ----------

    protected void responder(HttpExchange ex, int codigo, Object datos) throws IOException {
        byte[] cuerpo = GSON.toJson(datos).getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(codigo, cuerpo.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(cuerpo);
        }
    }

    protected void sinContenido(HttpExchange ex) throws IOException {
        ex.sendResponseHeaders(204, -1);
    }

    protected void error(HttpExchange ex, int codigo, String mensaje) throws IOException {
        responder(ex, codigo, Map.of("error", mensaje == null ? "Error" : mensaje));
    }

    protected void metodoNoPermitido(HttpExchange ex) throws IOException {
        error(ex, 405, "Método no permitido: " + ex.getRequestMethod());
    }

    // ---------- Leer la petición ----------

    // El token llega en la cabecera:  Authorization: Bearer <token>
    private String tokenDe(HttpExchange ex) {
        String cabecera = ex.getRequestHeaders().getFirst("Authorization");
        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            return null;
        }
        return cabecera.substring("Bearer ".length()).trim();
    }

    // El id al final de la dirección: /api/socios/3 → 3, /api/socios → null
    protected Integer idDeRuta(HttpExchange ex) {
        String base = ex.getHttpContext().getPath();
        String resto = ex.getRequestURI().getPath().substring(base.length());
        if (resto.isEmpty() || resto.equals("/")) {
            return null;
        }
        try {
            return Integer.parseInt(resto.substring(1));
        } catch (NumberFormatException e) {
            throw new NoEncontrado("No existe la ruta " + ex.getRequestURI().getPath());
        }
    }

    // Lo que va después del ? en la dirección: ?socioId=1&buscar=ana
    protected Map<String, String> parametros(HttpExchange ex) {
        Map<String, String> mapa = new HashMap<>();
        String query = ex.getRequestURI().getRawQuery();
        if (query == null) {
            return mapa;
        }
        for (String par : query.split("&")) {
            String[] partes = par.split("=", 2);
            String clave = URLDecoder.decode(partes[0], StandardCharsets.UTF_8);
            String valor = partes.length > 1 ? URLDecoder.decode(partes[1], StandardCharsets.UTF_8) : "";
            mapa.put(clave, valor);
        }
        return mapa;
    }

    protected int parametroEntero(HttpExchange ex, String nombre) {
        String valor = parametros(ex).get(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Falta el parámetro " + nombre);
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El parámetro " + nombre + " debe ser un número");
        }
    }

    protected JsonObject leerJson(HttpExchange ex) throws IOException {
        try (InputStream in = ex.getRequestBody()) {
            String texto = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            if (texto.isBlank()) {
                throw new IllegalArgumentException("Falta el cuerpo JSON de la petición");
            }
            return JsonParser.parseString(texto).getAsJsonObject();
        } catch (IllegalStateException | com.google.gson.JsonParseException e) {
            throw new IllegalArgumentException("El cuerpo no es un JSON válido");
        }
    }

    // Texto de un campo; si es obligatorio y falta o está vacío, error 400.
    // Los opcionales vacíos se guardan como null.
    protected String texto(JsonObject json, String campo, boolean obligatorio) {
        JsonElement e = json.get(campo);
        String valor = (e == null || e.isJsonNull()) ? null : e.getAsString().trim();
        if (valor == null || valor.isEmpty()) {
            if (obligatorio) {
                throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
            }
            return null;
        }
        return valor;
    }

    protected int entero(JsonObject json, String campo) {
        try {
            return json.get(campo).getAsInt();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio y debe ser un número entero");
        }
    }

    protected BigDecimal dinero(JsonObject json, String campo) {
        try {
            return json.get(campo).getAsBigDecimal();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio y debe ser un número");
        }
    }

    // Para los 404: socio, plan o ruta que no existe
    public static class NoEncontrado extends RuntimeException {
        public NoEncontrado(String mensaje) {
            super(mensaje);
        }
    }
}
