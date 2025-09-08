package Hospital.TableModel;

import  Hospital.Entidades.Paciente;
import java.util.List;

public class TableModelPacientes extends AbstractTableModel<Paciente> {

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int TELEFONO = 2;
    public static final int FECNACIMIENTO = 3;

    public TableModelPacientes(int[] col, List<Paciente> rows) {
        super(col, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[ID]            = "ID";
        colNames[NOMBRE]        = "NOMBRE";
        colNames[TELEFONO]      = "TELEFONO";
        colNames[FECNACIMIENTO] = "Fecha Nacimiento";
    }

    @Override
    protected Object getPropetyAt(Paciente paciente, int col) {
        switch (cols[col]) {
            case ID:             return paciente.getId();
            case NOMBRE:         return paciente.getNombre();
            case TELEFONO:       return paciente.getNumeroTelefonico();
            case FECNACIMIENTO:  return paciente.getFechaNacimiento();
            default:             return "";
        }
    }
}

