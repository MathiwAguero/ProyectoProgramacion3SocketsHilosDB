package Controller;

import ManejoListas.Factory;
import Entidades.Medico;
import Exceptions.DataAccessException;
import Model.ModelMedico;
import View.Medicos;
import java.util.List;
import java.util.stream.Collectors;

public class MedicoController {
    Medicos view;
    ModelMedico model;

    public MedicoController(Medicos view, ModelMedico model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        try {
            List<Medico> medicos = Factory.get().medico().obtenerTodos();
            model.setList(medicos);
            model.setCurrent(new Medico());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Medico medico) throws DataAccessException {
        try {
            if(Factory.get().medico().existeId(medico.getId())) {
                Factory.get().medico().actualizar(medico);
            } else {
                Factory.get().medico().insertar(medico);
            }
            model.setCurrent(new Medico());
            model.setList(Factory.get().medico().obtenerTodos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar el medico" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            Medico encontrado =  Factory.get().medico().obtenerPorId(id);
            if (encontrado == null) {
                throw new DataAccessException("Medico no encontrado");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            Medico m = new Medico();
            m.setId(id);
            model.setCurrent(m);
            throw ex;
        }
    }

    public void delete(String id) throws DataAccessException {
        Factory.get().medico().eliminar(id);
        model.setCurrent(new Medico());
        model.setList(Factory.get().medico().obtenerTodos());
    }

    public void search(String search) throws DataAccessException {
        List<Medico> general = Factory.get().medico().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Medico> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }


    public void clear() {
        model.setCurrent(new Medico());
        model.setList(Factory.get().medico().obtenerTodos());
    }
}