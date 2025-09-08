package Hospital.TableModel;

import  Hospital.Entidades.Medicamento;
import java.util.List;

public class TableModelMedicamentos extends AbstractTableModel<Medicamento> {

    public static final int CODIGO = 0;
    public static final int NOMBRE = 1;
    public static final int PRESENTACION = 2;

    public TableModelMedicamentos(int[] col, List<Medicamento> rows) {
        super(col, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[CODIGO] = "CODIGO";
        colNames[NOMBRE] = "NOMBRE";
        colNames[PRESENTACION] = "PRESENTACION";
    }

    @Override
    protected Object getPropetyAt(Medicamento medicamento, int col) {
        switch (cols[col]) {
            case CODIGO:        return medicamento.getCodigo();
            case NOMBRE:        return medicamento.getNombre();
            case PRESENTACION:  return medicamento.getPresentacion();
            default:            return "";
        }
    }
}
