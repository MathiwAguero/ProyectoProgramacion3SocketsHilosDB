package hospital.Presentation.Usuarios;

import hospital.Entities.Entities.UsuarioBase;
import hospital.Presentation.AbstractModel.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ModelUsuariosMensaje extends AbstractModel {
    private UsuarioBase current;
    private List<UsuarioBase> list;
    private List<String> mensajes; // Historial de mensajes

    public static final String CURRENT = "current";
    public static final String LIST = "list";
    public static final String MENSAJES = "mensajes";

    public ModelUsuariosMensaje() {
        current = new UsuarioBase();
        list = new ArrayList<>();
        mensajes = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(MENSAJES);
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

    public List<String> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<String> mensajes) {
        this.mensajes = mensajes;
        firePropertyChange(MENSAJES);
    }

    public void agregarMensaje(String mensaje) {
        if (this.mensajes == null) {
            this.mensajes = new ArrayList<>();
        }
        this.mensajes.add(mensaje);
        firePropertyChange(MENSAJES);
    }
}