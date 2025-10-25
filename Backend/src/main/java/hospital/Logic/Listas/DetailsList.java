package hospital.Logic.Listas;

import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.*;

import java.util.List;

public class DetailsList extends Base<RecipeDetails> {

    @Override
    public void insertar(RecipeDetails d) throws DataAccessException {
        if (d == null || d.getCodigoMedicamento() == null)
            throw new DataAccessException("Detalle inválido");
        if (existeId(d.getCodigoMedicamento()))
            throw new DataAccessException("Detalle ya existe para: " + d.getCodigoMedicamento());
        data.getDetalles().add(d);
        guardarEnXML();
    }

    @Override
    public RecipeDetails obtenerPorId(String codigoMedicamento) {
        if (codigoMedicamento == null) return null;
        return data.getDetalles().stream()
                .filter(x -> codigoMedicamento.equals(x.getCodigoMedicamento()))
                .findFirst().orElse(null);
    }

    @Override
    public List<RecipeDetails> obtenerTodos() { return data.getDetalles(); }

    @Override
    public void actualizar(RecipeDetails d) throws DataAccessException {
        if (d == null || d.getCodigoMedicamento() == null)
            throw new DataAccessException("Detalle inválido");
        for (int i = 0; i < data.getDetalles().size(); i++) {
            if (data.getDetalles().get(i).getCodigoMedicamento().equals(d.getCodigoMedicamento())) {
                data.getDetalles().set(i, d);
                guardarEnXML();
                return;
            }
        }
        throw new DataAccessException("Detalle no encontrado: " + d.getCodigoMedicamento());
    }

    @Override
    public void eliminar(String codigoMedicamento) throws DataAccessException {
        boolean ok = data.getDetalles().removeIf(x -> x.getCodigoMedicamento().equals(codigoMedicamento));
        if (!ok) throw new DataAccessException("No existe detalle para: " + codigoMedicamento);
        guardarEnXML();
    }

    @Override
    public boolean existeId(String codigoMedicamento) {
        return codigoMedicamento != null &&
                data.getDetalles().stream().anyMatch(x -> codigoMedicamento.equals(x.getCodigoMedicamento()));
    }
}

