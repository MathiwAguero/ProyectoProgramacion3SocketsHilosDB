package hospital.Logic.Listas;

import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.*;

import java.util.List;

public class AdminList extends Base<Admin> {

    @Override
    public void insertar(Admin a) throws DataAccessException {
        if (a == null || a.getId() == null) throw new DataAccessException("Administrador invalido");
        if (existeId(a.getId())) throw new DataAccessException("Administrador ya existe: " + a.getId());
        data.getAdmins().add(a);
        guardarEnXML();
    }

    @Override
    public Admin obtenerPorId(String id) {
        if (id == null) return null;
        return data.getAdmins().stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Admin> obtenerTodos() { return data.getAdmins(); }

    @Override
    public void actualizar(Admin a) throws DataAccessException {
        if (a == null || a.getId() == null) throw new DataAccessException("Administrador invalido");
        for (int i = 0; i < data.getAdmins().size(); i++) {
            if (data.getAdmins().get(i).getId().equals(a.getId())) {
                data.getAdmins().set(i, a);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Administrador no encontrado: " + a.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = data.getAdmins().removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe administrador: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && data.getAdmins().stream().anyMatch(x -> id.equals(x.getId()));
    }
}
