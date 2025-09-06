package Model;
import Entidades.RecipeDetails;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelDetails extends AbstractModel {
    RecipeDetails current;
    List<RecipeDetails> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public ModelDetails() {
        current = new RecipeDetails();
        list = new ArrayList<RecipeDetails>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public RecipeDetails getCurrent() {
        return current;
    }

    public void setCurrent(RecipeDetails current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<RecipeDetails> getList() {
        return list;
    }

    public void setList(List<RecipeDetails> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}
