package hospital.Logic.Listas;

import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.*;

import java.util.List;

public class RecetaList extends Base<Receta> {

    @Override
    public void insertar(Receta r) throws DataAccessException {
        if (r == null || r.getId() == null) throw new DataAccessException("Receta inválida");
        if (existeId(r.getId())) throw new DataAccessException("Receta ya existe: " + r.getId());
        data.getRecetas().add(r);
        guardarEnXML();
    }

    @Override
    public Receta obtenerPorId(String id) {
        if (id == null) return null;
        return data.getRecetas().stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Receta> obtenerTodos() { return data.getRecetas(); }

    @Override
    public void actualizar(Receta r) throws DataAccessException {
        if (r == null || r.getId() == null) throw new DataAccessException("Receta inválida");
        for (int i = 0; i < data.getRecetas().size(); i++) {
            if (data.getRecetas().get(i).getId().equals(r.getId())) {
                data.getRecetas().set(i, r);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Receta no encontrada: " + r.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = data.getRecetas().removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe receta: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && data.getRecetas().stream().anyMatch(x -> id.equals(x.getId()));
    }
}
