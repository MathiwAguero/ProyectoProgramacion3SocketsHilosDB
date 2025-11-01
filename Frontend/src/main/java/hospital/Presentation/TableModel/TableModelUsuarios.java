package hospital.Presentation.TableModel;

import hospital.Entities.Entities.UsuarioBase;
import hospital.Presentation.Usuarios.Usuarios;

import javax.swing.table.TableModel;
import java.util.List;

public class TableModelUsuarios extends AbstractTableModel<UsuarioBase> {
    public static final int ID=0;
    public TableModelUsuarios(int[] cols, List<UsuarioBase> rows) {
        super(cols, rows);
    }


    @Override
    protected Object getPropetyAt(UsuarioBase usuarios, int col) {
        if(usuarios==null){return "";}
        switch(cols[col]){
            case ID: return usuarios.getId() != null ? usuarios.getId() : "Sin ID";
            default: return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[1];
        colNames[0] = "ID";

    }
}
