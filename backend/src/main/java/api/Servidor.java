package api;

import com.sun.net.httpserver.HttpServer;
import conexion.Conexion;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

// Arranca la API del gimnasio. El front la llama con fetch("http://localhost:8080/api/...").
// Endpoints y JSON: docs/api.md
public class Servidor {

    public static void main(String[] args) throws Exception {
        // El puerto se puede cambiar con la variable de entorno PORT (útil para subirlo a la nube)
        int puerto = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        HttpServer servidor = HttpServer.create(new InetSocketAddress(puerto), 0);
        servidor.createContext("/api/login", new LoginRuta());
        servidor.createContext("/api/dashboard", new DashboardRuta());
        servidor.createContext("/api/socios", new SociosRuta());
        servidor.createContext("/api/planes", new PlanesRuta());
        servidor.createContext("/api/membresias", new MembresiasRuta());
        servidor.createContext("/api/pagos", new PagosRuta());
        servidor.setExecutor(Executors.newFixedThreadPool(8));   // varias peticiones a la vez
        servidor.start();

        System.out.println("API del gimnasio funcionando en http://localhost:" + puerto + "/api");
        System.out.println("Base de datos: " + Conexion.getBaseDatos() + " (" + Conexion.getDondeEsta() + ")");
        System.out.println("Para pararla: Ctrl + C");
    }
}
