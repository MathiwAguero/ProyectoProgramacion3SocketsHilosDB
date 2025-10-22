package hospital.Presentation.TableModel;

import Logic.Entities.RecipeDetails;
import java.util.List;

public class TableModelDetails extends AbstractTableModel<RecipeDetails> {

    public static final int MEDICAMENTO = 0;
    public static final int CANTIDAD    = 1;
    public static final int INDICACIONES= 2;
    public static final int DURACION    = 3;

    public TableModelDetails(int[] col, List<RecipeDetails> rows) {
        super(col, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[MEDICAMENTO] = "MEDICAMENTO";
        colNames[CANTIDAD]    = "CANTIDAD";
        colNames[INDICACIONES]= "INDICACIONES";
        colNames[DURACION]    = "DURACION";
    }

    @Override
    protected Object getPropetyAt(RecipeDetails d, int col) {
        switch (cols[col]) {
            case MEDICAMENTO: return d.getCodigoMedicamento();
            case CANTIDAD:    return d.getCantidad();
            case INDICACIONES:return d.getIndicaciones();
            case DURACION:    return d.getDuracionTratamiento();
            default:          return "";
        }
    }
}
