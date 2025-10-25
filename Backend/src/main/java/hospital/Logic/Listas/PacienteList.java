package hospital.Logic.Listas;

import Logic.Entities.Paciente;
import Logic.Exceptions.DataAccessException;

import java.util.List;

public class PacienteList extends Base<Paciente> {

    @Override
    public void insertar(Paciente p) throws DataAccessException {
        if (p == null || p.getId() == null) throw new DataAccessException("Paciente inválido");
        if (existeId(p.getId())) throw new DataAccessException("Paciente ya existe: " + p.getId());
        data.getPacientes().add(p);
        guardarEnXML();
    }

    @Override
    public Paciente obtenerPorId(String id) {
        if (id == null) return null;
        return data.getPacientes().stream().filter(x -> id.equals(x.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Paciente> obtenerTodos() { return data.getPacientes(); }

    @Override
    public void actualizar(Paciente p) throws DataAccessException {
        if (p == null || p.getId() == null) throw new DataAccessException("Paciente inválido");
        for (int i = 0; i < data.getPacientes().size(); i++) {
            if (data.getPacientes().get(i).getId().equals(p.getId())) {
                data.getPacientes().set(i, p);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Paciente no encontrado: " + p.getId());
    }

    @Override
    public void eliminar(String id) throws DataAccessException {
        boolean ok = data.getPacientes().removeIf(x -> x.getId().equals(id));
        if (!ok) throw new DataAccessException("No existe paciente: " + id);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String id) {
        return id != null && data.getPacientes().stream().anyMatch(x -> id.equals(x.getId()));
    }
}

