package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Una fila de la tabla pago. Cada pago crea (o renueva) una membresía.
public class Pago {
    public static final List<String> METODOS = List.of("EFECTIVO", "TARJETA", "TRANSFERENCIA");

    private int id;
    private int membresiaId;
    private LocalDate fecha;
    private BigDecimal monto;
    private String metodo;
    private Membresia membresia;   // la membresía que paga este pago (fechas y estado)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMembresiaId() { return membresiaId; }
    public void setMembresiaId(int membresiaId) { this.membresiaId = membresiaId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public Membresia getMembresia() { return membresia; }
    public void setMembresia(Membresia membresia) { this.membresia = membresia; }

    @Override
    public String toString() {
        return id + " | " + fecha + " | $" + monto + " | " + metodo
                + " | " + membresia.getNombrePlan() + " " + membresia.getFechaInicio()
                + " → " + membresia.getFechaFin() + " | " + membresia.getEstado();
    }
}
