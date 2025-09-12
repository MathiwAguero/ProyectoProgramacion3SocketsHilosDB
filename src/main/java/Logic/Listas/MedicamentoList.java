package Logic.Listas;

import Logic.Entities.Medicamento;
import Logic.Exceptions.DataAccessException;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoList extends Base<Medicamento> {

    @Override
    public void insertar(Medicamento m) throws DataAccessException {
        if (m == null || m.getCodigo() == null) throw new DataAccessException("Medicamento inválido");
        if (existeId(m.getCodigo())) throw new DataAccessException("Medicamento ya existe: " + m.getCodigo());
        data.getMedicamentos().add(m);
        guardarEnXML();
    }

    @Override
    public Medicamento obtenerPorId(String codigo) {
        if (codigo == null) return null;
        return data.getMedicamentos().stream().filter(x -> codigo.equals(x.getCodigo())).findFirst().orElse(null);
    }


    @Override
    public List<Medicamento> obtenerTodos() { return data.getMedicamentos(); }

    public List<String> obtenerNombres() {
        List<String> nombres = new ArrayList<>();
        for (Medicamento m : data.getMedicamentos()) nombres.add(m.getNombre());
        return nombres;
    }

    @Override
    public void actualizar(Medicamento m) throws DataAccessException {
        if (m == null || m.getCodigo() == null) throw new DataAccessException("Medicamento inválido");
        for (int i = 0; i < data.getMedicamentos().size(); i++) {
            if (data.getMedicamentos().get(i).getCodigo().equals(m.getCodigo())) {
                data.getMedicamentos().set(i, m);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Medicamento no encontrado: " + m.getCodigo());
    }

    @Override
    public void eliminar(String codigo) throws DataAccessException {
        boolean ok = data.getMedicamentos().removeIf(x -> x.getCodigo().equals(codigo));
        if (!ok) throw new DataAccessException("No existe medicamento: " + codigo);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String codigo) {
        return codigo != null && data.getMedicamentos().stream().anyMatch(x -> codigo.equals(x.getCodigo()));
    }
}
