package Logic.Entities;

import com.toedter.calendar.JDateChooser;

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
    private JDateChooser fechaRecoleccion;
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
    public JDateChooser getFechaRecoleccion() { return fechaRecoleccion; }
    public void setFechaRecoleccion(JDateChooser fechaRecoleccion) {}

    public String getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(String fechaRetiro) {
        this.fechaRetiro = (fechaRetiro != null) ? fechaRetiro : "";
    }

    public EstadoReceta getEstado() { return estado; }
    public void setEstado(EstadoReceta estado) {
        this.estado = (estado != null) ? estado : EstadoReceta.PROCESO;
    }
    public int getCantidad() {
        int cantidad = 0;
        for(int i=0;i<detalles.size();i++) {
            cantidad += detalles.get(i).getCantidad();
        }
        return cantidad;
    }
    public List<RecipeDetails> getDetalles() { return detalles; }
    public void setDetalles(List<RecipeDetails> detalles) {
        this.detalles = (detalles != null) ? detalles : new ArrayList<>();
    }
    public String mostarListaDetalles() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<detalles.size();i++) {
            sb.append(detalles.get(i).toString());
        }
        return sb.toString();
    }
    public void agregarDetalle(RecipeDetails detalle) {
        if (detalle == null) return;
        if (this.detalles == null) this.detalles = new ArrayList<>();
        this.detalles.add(detalle);
    }

}