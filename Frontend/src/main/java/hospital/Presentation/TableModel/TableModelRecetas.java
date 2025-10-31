package hospital.Presentation.TableModel;

import hospital.Entities.Entities.*;
import java.util.List;

public class TableModelRecetas extends AbstractTableModel<Receta> {
    public static final int ID = 0;
    public static final int MEDICO = 1;
    public static final int PACIENTE = 2;
    public static final int ESTADO = 3;
    public static final int FECHARETIRO = 4;
    public static final int FECHACONFECC = 5;
    public static final int DETALLES = 6;

    public TableModelRecetas(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Receta rec, int col) {
        if (rec == null) return "";
        switch (cols[col]) {
            case ID:
                return rec.getId() != null ? rec.getId() : "Sin ID";
            case MEDICO:
                return rec.getMedico() != null && rec.getMedico().getId() != null
                        ? rec.getMedico().getId() : "No asignado";
            case PACIENTE:
                return rec.getPaciente() != null && rec.getPaciente().getNombre() != null
                        ? rec.getPaciente().getNombre() : "No asignado";
            case ESTADO:
                return rec.getEstado() != null ? rec.getEstado().toString() : "Sin estado";
            case FECHARETIRO:
                return rec.getFechaRetiro() != null ? rec.getFechaRetiro() : "";
            case FECHACONFECC:
                return rec.getFechaConfeccion() != null ? rec.getFechaConfeccion() : "";
            case DETALLES:
                return rec.getCantidad() ;
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[7];
        colNames[ID]           = "ID";
        colNames[MEDICO]       = "MEDICO";
        colNames[PACIENTE]     = "PACIENTE";
        colNames[ESTADO]       = "ESTADO";
        colNames[FECHARETIRO]  = "FECHA RETIRO";
        colNames[FECHACONFECC] = "FECHA CONFECCIÓN";
        colNames[DETALLES]     = "DETALLES";
    }
    public void setRows(List<Receta> rows) {
        this.rows = rows;
        fireTableDataChanged();
    }
}

