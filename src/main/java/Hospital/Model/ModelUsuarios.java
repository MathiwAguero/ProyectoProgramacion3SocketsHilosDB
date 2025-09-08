package Hospital.Model;
import Hospital.Entidades.UsuarioBase;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelUsuarios extends AbstractModel {
    UsuarioBase current;
    List<UsuarioBase> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public ModelUsuarios() {
        current = new UsuarioBase();
        list = new ArrayList<UsuarioBase>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public UsuarioBase getCurrent() {
        return current;
    }

    public void setCurrent(UsuarioBase current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<UsuarioBase> getList() {
        return list;
    }

    public void setList(List<UsuarioBase> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}
