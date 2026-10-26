package modelo;

import java.time.LocalDate;

// Una fila de la tabla socio
public class Socio {
    private int id;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaRegistro;
    private String estado;   // ACTIVA o VENCIDA: no es columna, se calcula con su última membresía

    public Socio() {
    }

    // Para crear un socio nuevo: el id lo pone la base de datos
    public Socio(String dni, String nombre, String apellido, String email, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return id + " | " + dni + " | " + nombre + " " + apellido
                + " | " + email + " | " + telefono + " | " + fechaRegistro + " | " + estado;
    }
}
