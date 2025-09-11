package Presentation.Dashboard;

import Logic.Entities.MedicamentosResumen;
import Presentation.AbstractModel.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelMedicamentosResumen extends AbstractModel {
    MedicamentosResumen current ;
    List<MedicamentosResumen> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public ModelMedicamentosResumen() {
        current= new MedicamentosResumen();
        list = new ArrayList<MedicamentosResumen>();
    }
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public MedicamentosResumen getCurrent() {
        return current;
    }

    public void setCurrent(MedicamentosResumen current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<MedicamentosResumen> getList() {
        return list;
    }

    public void setList(List<MedicamentosResumen> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
    }
