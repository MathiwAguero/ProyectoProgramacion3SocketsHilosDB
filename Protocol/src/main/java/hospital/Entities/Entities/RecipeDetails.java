package hospital.Entities.Entities;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeDetails {
    private String nombre;
    private String codigoMedicamento;
    private int cantidad;
    private String indicaciones;
    private int duracionTratamiento;

    public RecipeDetails() {}

    public RecipeDetails(String codigoMedicamento, int cantidad, String indicaciones, int duracionTratamiento) {
        this.codigoMedicamento = codigoMedicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionTratamiento = duracionTratamiento;
    }

    public String getCodigoMedicamento() { return codigoMedicamento; }
    public void setCodigoMedicamento(String codigoMedicamento) { this.codigoMedicamento = codigoMedicamento; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }
    public int getDuracionTratamiento() { return duracionTratamiento; }
    public void setDuracionTratamiento(int duracionTratamiento) { this.duracionTratamiento = duracionTratamiento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "\n" + "Medicamento: " + codigoMedicamento + "\n" +
                "Cantidad Medicamento: " + cantidad + "\n" +
                "Indicaciones: " + indicaciones + "\n";
    }
}
