package Presentation.Farmaceutas;

import Logic.Listas.Factory;
import Logic.Entities.Farmaceuta;
import Logic.Exceptions.DataAccessException;

import java.util.List;
import java.util.stream.Collectors;

public class FarmaceutaController {
    Farmaceutas view;
    ModelFarmaceuta model;

    public FarmaceutaController(Farmaceutas view, ModelFarmaceuta model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        try {
            List<Farmaceuta> farmaceutas = Factory.get().farmaceuta().obtenerTodos();
            model.setList(farmaceutas);
            model.setCurrent(new Farmaceuta());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Farmaceuta farmaceuta) throws DataAccessException {
        try {
            if(Factory.get().farmaceuta().existeId(farmaceuta.getId())) {
                Factory.get().farmaceuta().actualizar(farmaceuta);
            } else {
                Factory.get().farmaceuta().insertar(farmaceuta);
            }
            model.setCurrent(new Farmaceuta());
            model.setList(Factory.get().farmaceuta().obtenerTodos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar el farmaceuta" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            Farmaceuta encontrado =  Factory.get().farmaceuta().obtenerPorId(id);
            if (encontrado == null) {
                throw new DataAccessException("Medico no encontrado");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            Farmaceuta m = new Farmaceuta();
            m.setId(id);
            model.setCurrent(m);
            throw ex;
        }
    }

    public void delete(String id) throws DataAccessException {
        Factory.get().farmaceuta().eliminar(id);
        model.setCurrent(new Farmaceuta());
        model.setList(Factory.get().farmaceuta().obtenerTodos());
    }

    public void search(String search) throws DataAccessException {
        List<Farmaceuta> general = Factory.get().farmaceuta().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Farmaceuta> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void clear() {
        model.setCurrent(new Farmaceuta());
        model.setList(Factory.get().farmaceuta().obtenerTodos());
    }
}