package Entidades;


import java.util.ArrayList;
import java.util.List;

public class Receta {
    private String id;
    private Paciente paciente;
    private Medico medic;
    private String fechaConfeccion;
    private String fechaRetiro;
    private EstadoReceta estado;
    private List<RecipeDetails> detalles;

    public Receta() {
        this.detalles = new ArrayList<>();
        this.estado = EstadoReceta.CONFECCIONADA;
    }

    public Receta(String id, Paciente paciente, Medico medic, String fechaConfeccion, String fechaRetiro) {
        this.id = id;
        this.paciente = paciente;
        this.medic = medic;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = EstadoReceta.CONFECCIONADA;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Paciente getIdPaciente() { return paciente; }
    public void setIdPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getIdMedico() { return medic; }
    public void setIdMedico(String idMedico) { this.medic = medic; }

    public String getFechaConfeccion() { return fechaConfeccion; }
    public void setFechaConfeccion(String fechaConfeccion) { this.fechaConfeccion = fechaConfeccion; }

    public String getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(String fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    public EstadoReceta getEstado() { return estado; }
    public void setEstado(EstadoReceta estado) { this.estado = estado; }

    public List<RecipeDetails> getDetalles() { return detalles; }
    public void setDetalles(List<RecipeDetails> detalles) { this.detalles = detalles; }

    public void agregarDetalle(RecipeDetails detalle) {
        this.detalles.add(detalle);
    }

    public void eliminarDetalle(int indice) {
        if (indice >= 0 && indice < detalles.size()) {
            this.detalles.remove(indice);
        }
    }
}