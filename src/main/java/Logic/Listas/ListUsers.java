package Logic.Listas;

import Logic.Entities.UsuarioBase;
import Logic.Exceptions.DataAccessException;

import java.util.List;

public class ListUsers extends Base<UsuarioBase> {

    @Override
    public void insertar(UsuarioBase u) throws DataAccessException {
        if (u == null || u.getId() == null) throw new DataAccessException("Usuario inválido");
        if (existeId(u.getId())) throw new DataAccessException("Usuario ya existe: " + u.getId());
        data.getUsuarios().add(u);
        guardarEnXML();
    }

    @Override
    public UsuarioBase obtenerPorId(String id) {
        if (id == null) return null;
        return data.getUsuarios().stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<UsuarioBase> obtenerTodos() { return data.getUsuarios(); }

    @Override
    public void actualizar(UsuarioBase u) throws DataAccessException {
        if (u == null || u.getId() == null) throw new DataAccessException("Usuario inválido");
        for (int i = 0; i < data.getUsuarios().size(); i++) {
            if (data.getUsuarios().get(i).getId().equals(u.getId())) {
                data.getUsuarios().set(i, u);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Usuario no encontrado: " + u.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = data.getUsuarios().removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe usuario: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && data.getUsuarios().stream().anyMatch(x -> id.equals(x.getId()));
    }
}
