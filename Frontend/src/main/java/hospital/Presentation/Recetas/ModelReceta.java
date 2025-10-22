package hospital.Presentation.Recetas;
import Presentation.AbstractModel.AbstractModel;
import Logic.Entities.Receta;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelReceta extends AbstractModel {
    Receta current;
    List<Receta> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public ModelReceta() {
        current = new Receta();
        list = new ArrayList<Receta>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Receta getCurrent() {
        return current;
    }

    public void setCurrent(Receta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Receta> getList() {
        return list;
    }

    public void setList(List<Receta> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

}
