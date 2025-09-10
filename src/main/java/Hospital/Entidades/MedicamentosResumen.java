package Hospital.Entidades;

public class MedicamentosResumen {
    private String nombreMedicamento;
    private int cantidadTotal;

    public MedicamentosResumen() {
    }

    public MedicamentosResumen(String nombreMedicamento, int cantidadTotal) {
        this.nombreMedicamento = nombreMedicamento;
        this.cantidadTotal = cantidadTotal;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    @Override
    public String toString() {
        return "MedicamentoResumen{" +
                "nombreMedicamento='" + nombreMedicamento + '\'' +
                ", cantidadTotal=" + cantidadTotal +
                '}';
    }
}
