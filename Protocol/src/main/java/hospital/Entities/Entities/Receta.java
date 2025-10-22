package hospital.Entities.Entities;

import com.toedter.calendar.JDateChooser;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Receta {
    private String id;
    private Paciente paciente;
    private Medico medico;
    private String fechaConfeccion;
    private String fechaRetiro;
    private EstadoReceta estado;

    @XmlElementWrapper(name = "detalles")
    @XmlElement(name = "detalle")
    private List<RecipeDetails> detalles;

    @XmlTransient
    private JDateChooser fechaRecoleccion; // no serializable por JAXB

    public Receta() {
        this.id = "";
        this.paciente = null;
        this.medico = null;
        this.fechaConfeccion = "";
        this.fechaRetiro = "";
        this.estado = EstadoReceta.PROCESO;
        this.detalles = new ArrayList<>();
    }

    public Receta(String id, Paciente paciente, Medico medico,
                  String fechaConfeccion, String fechaRetiro,
                  EstadoReceta estado, List<RecipeDetails> detalles) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
        this.detalles = (detalles != null) ? detalles : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public String getFechaConfeccion() { return fechaConfeccion; }
    public void setFechaConfeccion(String fechaConfeccion) { this.fechaConfeccion = (fechaConfeccion != null) ? fechaConfeccion : ""; }
    public String getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(String fechaRetiro) { this.fechaRetiro = (fechaRetiro != null) ? fechaRetiro : ""; }
    public EstadoReceta getEstado() { return estado; }
    public void setEstado(EstadoReceta estado) { this.estado = (estado != null) ? estado : EstadoReceta.PROCESO; }
    public List<RecipeDetails> getDetalles() { return detalles; }
    public void setDetalles(List<RecipeDetails> detalles) { this.detalles = (detalles != null) ? detalles : new ArrayList<>(); }

    public JDateChooser getFechaRecoleccion() { return fechaRecoleccion; }
    public void setFechaRecoleccion(JDateChooser fechaRecoleccion) { /* intencionalmente vacío para JAXB */ }

    public int getCantidad() {
        int cantidad = 0;
        for (int i = 0; i < detalles.size(); i++) cantidad += detalles.get(i).getCantidad();
        return cantidad;
    }

    public String mostarListaDetalles() {
        if (detalles == null || detalles.isEmpty()) return "Sin detalles.";
        StringBuilder sb = new StringBuilder();
        for (RecipeDetails d : detalles) {
            String nombre = (d.getNombre() != null && !d.getNombre().isBlank()) ? d.getNombre() : "No se asigno medicamento";
            sb.append("Medicamento: ").append(nombre)
                    .append("\nCantidad: ").append(d.getCantidad())
                    .append("\nIndicaciones: ").append(d.getIndicaciones())
                    .append("\n\n");
        }
        return sb.toString().trim();
    }

    public void agregarDetalle(RecipeDetails detalle) {
        if (detalle == null) return;
        if (this.detalles == null) this.detalles = new ArrayList<>();
        this.detalles.add(detalle);
    }
}
