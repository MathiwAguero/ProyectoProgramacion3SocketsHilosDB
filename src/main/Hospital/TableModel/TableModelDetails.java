package TableModel;
import java.util.List;

import Model.RecipeDetails;

public class TableModelDetails extends TableModel<RecipeDetails> implements javax.swing.table.TableModel {
    public TableModelDetails(int[] col, List<RecipeDetails> rows) { super(col, rows);}

    public String getColNames(int colum) {
        return colNames[cols[colum]];
    }

    public static final int MEDICAMENTO = 0;
    public static final int CANTIDAD = 1;
    public static final int INDICACIONES = 2;
    public static final int DURACION = 3;

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[0] = "MEDICAMENTO";
        colNames[1] = "CANTIDAD";
        colNames[2] = "INDICACIONES";
        colNames[3] = "DURACION";
    }

    @Override
    public String getColumnName(int column) {
        return colNames[cols[column]];
    }
    @Override
    public Object getProperty(RecipeDetails recipeDetails, int col) {
        switch(cols[col]) {
            case MEDICAMENTO: return recipeDetails.getCodigoMedicamento();
            case CANTIDAD: return recipeDetails.getCantidad();
            case INDICACIONES: return recipeDetails.getIndicaciones();
            case DURACION: return recipeDetails.getDuracionTratamiento();
            default: return " ";
        }
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }
}
