package hospital.Logic.Listas;

import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.*;

import java.util.List;

public class FarmaceutaList extends Base<Farmaceuta> {

    @Override
    public void insertar(Farmaceuta f) throws DataAccessException {
        if (f == null || f.getId() == null) throw new DataAccessException("Farmaceuta inválido");
        if (existeId(f.getId())) throw new DataAccessException("Farmaceuta ya existe: " + f.getId());
        data.getFarmaceutas().add(f);
        guardarEnXML();
    }

    @Override
    public Farmaceuta obtenerPorId(String id) {
        if (id == null) return null;
        return data.getFarmaceutas().stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Farmaceuta> obtenerTodos() { return data.getFarmaceutas(); }

    @Override
    public void actualizar(Farmaceuta f) throws DataAccessException {
        if (f == null || f.getId() == null) throw new DataAccessException("Farmaceuta inválido");
        for (int i = 0; i < data.getFarmaceutas().size(); i++) {
            if (data.getFarmaceutas().get(i).getId().equals(f.getId())) {
                data.getFarmaceutas().set(i, f);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Farmaceuta no encontrado: " + f.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = data.getFarmaceutas().removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe farmaceuta: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && data.getFarmaceutas().stream().anyMatch(x -> id.equals(x.getId()));
    }
}
