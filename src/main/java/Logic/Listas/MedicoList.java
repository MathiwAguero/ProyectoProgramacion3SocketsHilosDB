package Logic.Listas;

import Logic.Entities.Medico;
import Logic.Exceptions.DataAccessException;

import java.util.List;

public class MedicoList extends Base<Medico> {

    @Override
    public void insertar(Medico m) throws DataAccessException {
        if (m == null || m.getId() == null) throw new DataAccessException("Médico inválido");
        if (existeId(m.getId())) throw new DataAccessException("Médico ya existe: " + m.getId());
        data.getMedicos().add(m);
        guardarEnXML();
    }

    @Override
    public Medico obtenerPorId(String id) {
        if (id == null) return null;
        return data.getMedicos().stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Medico> obtenerTodos() { return data.getMedicos(); }

    @Override
    public void actualizar(Medico m) throws DataAccessException {
        if (m == null || m.getId() == null) throw new DataAccessException("Médico inválido");
        for (int i = 0; i < data.getMedicos().size(); i++) {
            if (data.getMedicos().get(i).getId().equals(m.getId())) {
                data.getMedicos().set(i, m);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Médico no encontrado: " + m.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = data.getMedicos().removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe médico: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && data.getMedicos().stream().anyMatch(x -> id.equals(x.getId()));
    }
}
