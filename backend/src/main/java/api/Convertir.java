package api;

import modelo.Membresia;
import modelo.Pago;
import modelo.Plan;
import modelo.Socio;
import modelo.Usuario;
import java.util.LinkedHashMap;
import java.util.Map;

// Pasa los objetos del modelo al JSON exacto del contrato (docs/api.md).
// Las fechas salen como texto "2026-10-22".
public class Convertir {

    public static Map<String, Object> socio(Socio s) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", s.getId());
        m.put("dni", s.getDni());
        m.put("nombre", s.getNombre());
        m.put("apellido", s.getApellido());
        m.put("email", s.getEmail());
        m.put("telefono", s.getTelefono());
        m.put("fechaRegistro", s.getFechaRegistro().toString());
        m.put("estado", s.getEstado());
        return m;
    }

    public static Map<String, Object> plan(Plan p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        m.put("nombre", p.getNombre());
        m.put("duracionDias", p.getDuracionDias());
        m.put("precio", p.getPrecio());
        return m;
    }

    public static Map<String, Object> membresia(Membresia mem) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", mem.getId());
        m.put("socioId", mem.getSocioId());
        m.put("planId", mem.getPlanId());
        m.put("nombrePlan", mem.getNombrePlan());
        m.put("fechaInicio", mem.getFechaInicio().toString());
        m.put("fechaFin", mem.getFechaFin().toString());
        m.put("estado", mem.getEstado());
        return m;
    }

    public static Map<String, Object> pago(Pago p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        m.put("socioId", p.getMembresia().getSocioId());
        m.put("fecha", p.getFecha().toString());
        m.put("monto", p.getMonto());
        m.put("metodo", p.getMetodo());
        m.put("membresia", membresia(p.getMembresia()));
        return m;
    }

    public static Map<String, Object> usuario(Usuario u) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", u.getId());
        m.put("usuario", u.getUsuario());
        m.put("rol", u.getRol());
        return m;
    }
}
