package hospital.Presentation.Farmaceutas;
import hospital.Presentation.AbstractModel.AbstractModel;
import hospital.Entities.Entities.*;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelFarmaceuta extends AbstractModel {
    Farmaceuta current;
    List<Farmaceuta> list;
    String searchFilter;
    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public static final String FILTER = "filter";
    public ModelFarmaceuta() {
        current = new Farmaceuta();
        list = new ArrayList<Farmaceuta>();
        searchFilter = "";
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

    public String getSearchFilter() {
        return searchFilter;
    }


    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
        firePropertyChange(FILTER);
    }
}
