package Presentation.TableModel;

import java.util.List;

public abstract class AbstractTableModel<E>  extends javax.swing.table.AbstractTableModel  implements javax.swing.table.TableModel {

    protected List<E> rows;
    protected int[] cols;
    protected String[] colNames;

    public AbstractTableModel(int[] cols, List<E> rows){
        this.cols = (cols != null) ? cols : new int[0];
        this.rows = (rows != null) ? rows : java.util.Collections.emptyList();
        initColNames();
    }
    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int col){
        return colNames[cols[col]];
    }

    @Override
    public Class<?> getColumnClass(int col){
        switch (cols[col]){
            default: return super.getColumnClass(col);
        }
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        E e = rows.get(row);
        return getPropetyAt(e, col);
    }

    protected abstract Object getPropetyAt(E e, int col);

    public E getRowAt(int row) {
        return rows.get(row);
    }

    protected abstract void initColNames();
}

