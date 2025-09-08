package Hospital.Model;
import Hospital.Entidades.Farmaceuta;
import Hospital.Entidades.Medicamento;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelFarmaceuta extends AbstractModel {
    Farmaceuta current;
    List<Farmaceuta> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public ModelFarmaceuta() {
        current = new Farmaceuta();
        list = new ArrayList<Farmaceuta>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Farmaceuta getCurrent() {
        return current;
    }

    public void setCurrent(Farmaceuta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Farmaceuta> getList() {
        return list;
    }

    public void setList(List<Farmaceuta> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}
