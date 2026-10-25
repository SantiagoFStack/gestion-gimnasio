package modelo;

// Una persona del personal del gimnasio que entra al sistema (no es un socio).
// No guarda la contraseña: después del login no hace falta tenerla en memoria.
public class Usuario {
    private int id;
    private String usuario;
    private String rol;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return id + " | " + usuario + " | " + rol;
    }
}
