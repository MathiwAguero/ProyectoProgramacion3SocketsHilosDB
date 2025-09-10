package Hospital.TableModel;

import Hospital.Entidades.MedicamentosResumen;

import javax.swing.table.TableModel;
import java.util.List;

public class TableModelDashboard  extends AbstractTableModel<MedicamentosResumen> {
public static final int NOMBRE_MEDICAMENTO = 0;
public static final int NUMERO_MEDICAMENTO = 1;

public TableModelDashboard(int[]cols, List<MedicamentosResumen> medicamentosResumen) {
super(cols,medicamentosResumen);
}
@Override
    protected void initColNames() {
    colNames=new String[2];
    colNames[NOMBRE_MEDICAMENTO] = "NOMBRE MEDICAMENTO";
    colNames[NUMERO_MEDICAMENTO] = "CANTIDAD MEDICAMENTO";

}
    @Override
    protected Object getPropetyAt(MedicamentosResumen medicamento, int col) {
        if (medicamento == null) return "";

        switch (cols[col]) {
            case NOMBRE_MEDICAMENTO:
                return medicamento.getNombreMedicamento() != null ?
                        medicamento.getNombreMedicamento() : "";
            case NUMERO_MEDICAMENTO:
                return medicamento.getCantidadTotal();
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (cols[col]) {
            case NOMBRE_MEDICAMENTO:
                return String.class;
            case NUMERO_MEDICAMENTO:
                return Integer.class;
            default:
                return super.getColumnClass(col);
        }
    }
}
