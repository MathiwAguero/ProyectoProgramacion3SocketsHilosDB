package TableModel;

import Model.Medico;

import java.util.List;

public class TableModelMedicos extends TableModel<Medico> implements javax.swing.table.TableModel {
    public TableModelMedicos(int[] cols, List<Medico> rows) { super(cols, rows); }

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int ESPECIALIDAD = 2;

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID] = "ID";
        colNames[NOMBRE] = "NOMBRE";
        colNames[ESPECIALIDAD] = "ESPECIALIDAD";
    }

    @Override
    public String getColumnName(int column) {
        return colNames[cols[column]];
    }

    public String getColNames(int colum) {
        return colNames[cols[colum]];
    }
    @Override
    public Object getProperty(Medico medico, int col) {
        switch(cols[col]) {
            case ID: return medico.getId();
            case NOMBRE: return medico.getNombre();
            case ESPECIALIDAD: return medico.getEspecialidad();
            default: return " ";
        }
    }
    @Override
    public int getColumnCount() {
        return cols.length;
    }
}
