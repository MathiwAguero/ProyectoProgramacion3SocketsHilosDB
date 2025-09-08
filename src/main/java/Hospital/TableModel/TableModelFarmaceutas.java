package Hospital.TableModel;

import  Hospital.Entidades.Farmaceuta;
import java.util.List;

public class TableModelFarmaceutas extends AbstractTableModel<Farmaceuta> {
    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int ESPECIALIDAD = 2;

    public TableModelFarmaceutas(int[] cols, List<Farmaceuta> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID]           = "ID";
        colNames[NOMBRE]       = "NOMBRE";
    }

    @Override
    protected Object getPropetyAt(Farmaceuta m, int col) {
        switch (cols[col]) {
            case ID:           return m.getId();
            case NOMBRE:       return m.getNombre();
            default:           return "";
        }
    }
}
