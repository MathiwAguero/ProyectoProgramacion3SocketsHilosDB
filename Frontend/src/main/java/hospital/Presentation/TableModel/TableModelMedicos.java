package hospital.Presentation.TableModel;

import hospital.Entities.Entities.*;
import java.util.List;

public class TableModelMedicos extends AbstractTableModel<Medico> {
    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int ESPECIALIDAD = 2;

    public TableModelMedicos(int[] cols, List<Medico> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID]           = "ID";
        colNames[NOMBRE]       = "NOMBRE";
        colNames[ESPECIALIDAD] = "ESPECIALIDAD";
    }

    @Override
    protected Object getPropetyAt(Medico m, int col) {
        switch (cols[col]) {
            case ID:           return m.getId();
            case NOMBRE:       return m.getNombre();
            case ESPECIALIDAD: return m.getEspecialidad();
            default:           return "";
        }
    }
}
