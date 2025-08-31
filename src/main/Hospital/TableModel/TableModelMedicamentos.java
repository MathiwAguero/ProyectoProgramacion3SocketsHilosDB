package TableModel;

import Model.Medicamento;

import java.util.List;

public class TableModelMedicamentos extends TableModel<Medicamento> implements javax.swing.table.TableModel {
    public TableModelMedicamentos(int[] col, List<Medicamento> rows) { super(col, rows);}

    public static final int CODIGO = 0;
    public static final int NOMBRE = 1;
    public static final int PRESENTACION = 2;

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[0] = "CODIGO";
        colNames[1] = "NOMBRE";
        colNames[2] = "PRESENTACION";
    }
    @Override
    public String getColumnName(int column) {
        return colNames[cols[column]];
    }
    public String getColNames(int colum) {
        return colNames[cols[colum]];
    }
    @Override
    public Object getProperty(Medicamento medicamento, int col) {
        switch (cols[col]) {
            case CODIGO:  return medicamento.getCodigo();
            case NOMBRE: return medicamento.getNombre();
            case PRESENTACION: return medicamento.getPresentacion();
            default: return "";
        }
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }
}
