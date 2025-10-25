package hospital.Entities.Entities;

import java.io.Serializable;
import java.util.Objects;

public class MedicamentosResumen implements Serializable {
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
    @Override
    public int hashCode() {
        return Objects.hash(this.getNombreMedicamento());
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null||getClass()!=obj.getClass()) return false;
        Admin other = (Admin) obj;
        return Objects.equals(this.getNombreMedicamento(),((MedicamentosResumen) obj).getNombreMedicamento());
    }
}
