package Model;
import Entidades.Medico;
import Entidades.Paciente;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelPaciente extends AbstractModel {
    Paciente current;
    List<Paciente> list;

    public static final String CURRENT = "current";
    public static final String LIST = "list";

    public ModelPaciente() {
        current = new Paciente();
        list = new ArrayList<Paciente>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public Paciente getCurrent() {
        return current;
    }

    public void setCurrent(Paciente current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Paciente> getList() {
        return list;
    }

    public void setList(List<Paciente> list) {
        this.list = list;
        firePropertyChange(LIST);
    }
}
