package TableModel;

import java.util.List;

public abstract class TableModel<T> extends javax.swing.table.AbstractTableModel implements javax.swing.table.TableModel {

    protected List<T> rows; //lista a usar en filas
    protected int[] cols; //columnas
    protected String[] colNames; //nombre de esas columnas para la tabla

    protected abstract void initColNames();

    public TableModel(int[] cols, List<T> rows) {
        this.cols = cols;
        this.rows = rows;
        initColNames();
    }

    public int getColu() {
        return cols.length;
    }

    public String getColumnName(int column) {
        return colNames[cols[column]];
    }

    public Class<?> getColumClass(int col) {
        switch (cols[col]) {
            default:
                return super.getColumnClass(col);
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int col) {
        T t = rows.get(row);
        return getProperty(t, col);
    }

    public abstract Object getProperty(T t, int col);
    public T getRow(int row) {
        return rows.get(row);
    }

}