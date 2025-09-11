package Presentation.Detalles;

import Logic.Entities.RecipeDetails;
import Logic.Listas.Factory;
import Logic.Exceptions.DataAccessException;

import java.util.List;
import java.util.stream.Collectors;

public class DetailsController {
    ModelDetails model;

    public DetailsController(ModelDetails model) {
        this.model = model;
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        try {
            List<RecipeDetails> details = Factory.get().details().obtenerTodos();
            model.setList(details);
            model.setCurrent(new RecipeDetails());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(RecipeDetails details) throws DataAccessException {
        try {
            if(Factory.get().details().existeId(details.getCodigoMedicamento())) {
                Factory.get().details().actualizar(details);
            } else {
                Factory.get().details().insertar(details);
            }
            model.setCurrent(new RecipeDetails());
            model.setList(Factory.get().details().obtenerTodos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error con la prescripcion de detalles" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            RecipeDetails encontrado =  Factory.get().details().obtenerPorId(id);
            if (encontrado == null) {
                throw new DataAccessException("No encontrado");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            RecipeDetails m = new RecipeDetails();
            m.setCodigoMedicamento(id);
            model.setCurrent(m);
            throw ex;
        }
    }

    public void delete(String id) throws DataAccessException {
        Factory.get().details().eliminar(id);
        model.setCurrent(new RecipeDetails());
        model.setList(Factory.get().details().obtenerTodos());
    }

    public void search(String search) throws DataAccessException {
        List<RecipeDetails> general = Factory.get().details().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<RecipeDetails> filtro = general.stream().filter(m -> m.getCodigoMedicamento() != null && m.getCodigoMedicamento().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void clear() {
        model.setCurrent(new RecipeDetails());
        model.setList(Factory.get().details().obtenerTodos());
    }
}