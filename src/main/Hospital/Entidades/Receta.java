package Entidades;

import java.util.ArrayList;
import java.util.List;

public class Receta {
    private String id;
    private Paciente paciente;
    private Medico medico;
    private String fechaConfeccion;
    private String fechaRetiro;
    private EstadoReceta estado;
    private List<RecipeDetails> detalles;

    public Receta() {
        this.id = "";
        this.paciente = null;
        this.medico = null;
        this.fechaConfeccion = "";
        this.fechaRetiro = "";
        this.estado = EstadoReceta.PROCESO;
        this.detalles = new ArrayList<>();
    }

    public Receta(String id, Paciente paciente, Medico medico, String fechaConfeccion,
                  String fechaRetiro, EstadoReceta estado, List<RecipeDetails> detalles) {

        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
        this.detalles = detalles;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String getFechaConfeccion() { return fechaConfeccion; }
    public void setFechaConfeccion(String fechaConfeccion) {
        this.fechaConfeccion = (fechaConfeccion != null) ? fechaConfeccion : "";
    }

    public String getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(String fechaRetiro) {
        this.fechaRetiro = (fechaRetiro != null) ? fechaRetiro : "";
    }

    public EstadoReceta getEstado() { return estado; }
    public void setEstado(EstadoReceta estado) {
        this.estado = (estado != null) ? estado : EstadoReceta.PROCESO;
    }

    public List<RecipeDetails> getDetalles() { return detalles; }
    public void setDetalles(List<RecipeDetails> detalles) {
        this.detalles = (detalles != null) ? detalles : new ArrayList<>();
    }

    public void agregarDetalle(RecipeDetails detalle) {
        if (detalle == null) return;
        if (this.detalles == null) this.detalles = new ArrayList<>();
        this.detalles.add(detalle);
    }

    public void eliminarDetalle(int indice) {
        if (this.detalles == null) return;
        if (indice >= 0 && indice < this.detalles.size()) {
            this.detalles.remove(indice);
        }
    }
}