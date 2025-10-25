package hospital.Entities.Entities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Receta implements Serializable {
    private static final long serialVersionUID = 1L;

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

    // ==================== GETTERS Y SETTERS ====================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getFechaConfeccion() {
        return fechaConfeccion;
    }

    public void setFechaConfeccion(String fechaConfeccion) {
        this.fechaConfeccion = (fechaConfeccion != null) ? fechaConfeccion : "";
    }

    public String getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(String fechaRetiro) {
        this.fechaRetiro = (fechaRetiro != null) ? fechaRetiro : "";
    }

    public EstadoReceta getEstado() {
        return estado;
    }

    public void setEstado(EstadoReceta estado) {
        this.estado = (estado != null) ? estado : EstadoReceta.PROCESO;
    }

    public List<RecipeDetails> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RecipeDetails> detalles) {
        this.detalles = (detalles != null) ? detalles : new ArrayList<>();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Calcula la cantidad total de medicamentos en la receta
     */
    public int getCantidad() {
        if (detalles == null) return 0;
        return detalles.stream()
                .mapToInt(RecipeDetails::getCantidad)
                .sum();
    }

    /**
     * Genera un texto con todos los detalles de la receta
     */
    public String mostarListaDetalles() {
        if (detalles == null || detalles.isEmpty()) {
            return "Sin detalles.";
        }

        StringBuilder sb = new StringBuilder();
        for (RecipeDetails d : detalles) {
            String nombre = (d.getNombre() != null && !d.getNombre().isBlank())
                    ? d.getNombre()
                    : "No se asignó medicamento";

            sb.append("Medicamento: ").append(nombre)
                    .append("\nCantidad: ").append(d.getCantidad())
                    .append("\nIndicaciones: ").append(d.getIndicaciones())
                    .append("\nDuración: ").append(d.getDuracionTratamiento()).append(" días")
                    .append("\n\n");
        }
        return sb.toString().trim();
    }

    /**
     * Agrega un detalle a la lista de medicamentos
     */
    public void agregarDetalle(RecipeDetails detalle) {
        if (detalle == null) return;
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
    }

    // ==================== EQUALS Y HASHCODE ====================

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Receta other = (Receta) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Receta{id='" + id + "', paciente=" +
                (paciente != null ? paciente.getNombre() : "null") +
                ", estado=" + estado + "}";
    }
}
