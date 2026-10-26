package modelo;

import java.math.BigDecimal;

// Una fila de la tabla plan
public class Plan {
    private int id;
    private String nombre;
    private int duracionDias;
    private BigDecimal precio;   // BigDecimal para dinero: no pierde decimales como double

    public Plan() {
    }

    // Para crear un plan nuevo: el id lo pone la base de datos
    public Plan(String nombre, int duracionDias, BigDecimal precio) {
        this.nombre = nombre;
        this.duracionDias = duracionDias;
        this.precio = precio;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getDuracionDias() { return duracionDias; }
    public void setDuracionDias(int duracionDias) { this.duracionDias = duracionDias; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    @Override
    public String toString() {
        return id + " | " + nombre + " | " + duracionDias + " días | $" + precio;
    }
}
