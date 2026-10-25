package api;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import modelo.Usuario;

// Token de sesión (issue #33): una "pulsera" que la API entrega al hacer login.
// Lleva quién es el usuario y hasta cuándo vale, y una firma hecha con una clave
// secreta que solo conoce el servidor: si alguien cambia algo, la firma ya no cuadra.
public class Token {
    private static final long DURACION_MS = 12L * 60 * 60 * 1000;   // 12 horas
    private static final byte[] SECRETO = leerSecreto();

    // En Render la clave viene de la variable TOKEN_SECRETO. En el PC, si no existe,
    // se inventa una al arrancar (las sesiones se pierden al reiniciar la API).
    private static byte[] leerSecreto() {
        String variable = System.getenv("TOKEN_SECRETO");
        if (variable != null && !variable.isBlank()) {
            return variable.getBytes(StandardCharsets.UTF_8);
        }
        byte[] aleatorio = new byte[32];
        new SecureRandom().nextBytes(aleatorio);
        return aleatorio;
    }

    public static String crear(Usuario u) {
        long expira = System.currentTimeMillis() + DURACION_MS;
        String datos = u.getId() + ":" + u.getUsuario() + ":" + u.getRol() + ":" + expira;
        return base64(datos.getBytes(StandardCharsets.UTF_8)) + "." + base64(firmar(datos));
    }

    // true si el token tiene una firma correcta y todavía no ha caducado
    public static boolean esValido(String token) {
        if (token == null || !token.contains(".")) {
            return false;
        }
        try {
            String[] partes = token.split("\\.", 2);
            String datos = new String(Base64.getUrlDecoder().decode(partes[0]), StandardCharsets.UTF_8);
            byte[] firma = Base64.getUrlDecoder().decode(partes[1]);
            if (!MessageDigest.isEqual(firma, firmar(datos))) {
                return false;
            }
            long expira = Long.parseLong(datos.substring(datos.lastIndexOf(':') + 1));
            return System.currentTimeMillis() < expira;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static byte[] firmar(String datos) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRETO, "HmacSHA256"));
            return mac.doFinal(datos.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo firmar el token", e);
        }
    }

    private static String base64(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
