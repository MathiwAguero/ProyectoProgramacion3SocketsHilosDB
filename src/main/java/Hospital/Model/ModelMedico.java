package Hospital.Model;
import Hospital.Entidades.Medico;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelMedico extends AbstractModel {
    Medico current;
    List<Medico> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public ModelMedico() {
        current = new Medico();
        list = new ArrayList<Medico>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Medico getCurrent() {
        return current;
    }

    public void setCurrent(Medico current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Medico> getList() {
        return list;
    }

    public void setList(List<Medico> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}
