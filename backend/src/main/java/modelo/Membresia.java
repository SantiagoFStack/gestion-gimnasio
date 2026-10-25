package modelo;

import java.time.LocalDate;

// Una fila de la tabla membresia: un socio que contrató un plan entre dos fechas
public class Membresia {
    public static final String ACTIVA = "ACTIVA";
    public static final String VENCIDA = "VENCIDA";

    private int id;
    private int socioId;
    private int planId;
    private String nombrePlan;   // no es columna de membresia: viene de la tabla plan
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // El estado no se guarda en la base: se calcula comparando la fecha de fin con hoy.
    // Si no hay fecha (socio sin membresías) cuenta como VENCIDA.
    public static String calcularEstado(LocalDate fechaFin) {
        if (fechaFin != null && !LocalDate.now().isAfter(fechaFin)) {
            return ACTIVA;
        }
        return VENCIDA;
    }

    public String getEstado() {
        return calcularEstado(fechaFin);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSocioId() { return socioId; }
    public void setSocioId(int socioId) { this.socioId = socioId; }

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public String getNombrePlan() { return nombrePlan; }
    public void setNombrePlan(String nombrePlan) { this.nombrePlan = nombrePlan; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    @Override
    public String toString() {
        return id + " | socio " + socioId + " | " + nombrePlan + " | "
                + fechaInicio + " → " + fechaFin + " | " + getEstado();
    }
}
