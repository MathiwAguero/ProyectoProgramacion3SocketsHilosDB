package TableModel;

import Model.Paciente;

import java.util.List;

public class TableModelPacientes extends TableModel<Paciente> implements javax.swing.table.TableModel {
    public TableModelPacientes(int[] col, List<Paciente> rows) { super(col, rows); }


    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int TELEFONO = 2;
    public static final int FECNACIMIENTO = 3;

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[0] = "ID";
        colNames[1] = "NOMBRE";
        colNames[2] = "TELEFONO";
        colNames[3] = "Fecha Nacimiento";

    }

    @Override
    public Object getProperty(Paciente paciente, int col) {
        switch(cols[col]) {
            case ID: return paciente.getId();
            case NOMBRE: return paciente.getNombre();
            case TELEFONO: return paciente.getNumeroTelefonico();
            case FECNACIMIENTO: return paciente.getFechaNacimiento();
            default: return " ";
        }
    }
    @Override
    public String getColumnName(int column) {
        return colNames[cols[column]];
    }
    public String getColNames(int colum) {
        return colNames[cols[colum]];
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }
}
