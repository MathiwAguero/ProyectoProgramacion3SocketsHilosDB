package Model;

public class Paciente {
    private String id;
    private String nombre;
    private String fechaNacimiento;
    private String numeroTelefonico;

    public Paciente() {}

    public Paciente(String id, String nombre, String fechaNacimiento, String numeroTelefonico) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNumeroTelefonico() { return numeroTelefonico; }
    public void setNumeroTelefonico(String numeroTelefonico) { this.numeroTelefonico = numeroTelefonico; }
}