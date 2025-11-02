package hospital.Presentation.TableModel;

import hospital.Entities.Entities.UsuarioBase;
import hospital.Presentation.Usuarios.Usuarios;

import javax.swing.table.TableModel;
import java.util.List;
import java.util.Map;

public class TableModelUsuarios extends AbstractTableModel<UsuarioBase> {
    public static final int ID = 0;

    private Map<String, Integer> contadorMensajes;

    public TableModelUsuarios(int[] cols, List<UsuarioBase> rows) {
        super(cols, rows);

    }

    @Override
    protected Object getPropetyAt(UsuarioBase usuario, int col) {
        if (usuario == null) {
            return "";
        }

        switch (cols[col]) {
            case ID:
                return usuario.getId() != null ? usuario.getId() : "Sin ID";

            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID] = "ID Usuario";

    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (cols[col]) {

            default:
                return String.class;
        }
    }

    public void actualizarContador(Map<String, Integer> nuevoContador) {
        this.contadorMensajes = nuevoContador;
        fireTableDataChanged();
    }
}