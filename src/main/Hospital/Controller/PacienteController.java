package Controller;

import Entidades.Medico;
import ManejoListas.Factory;
import Entidades.Paciente;
import Exceptions.DataAccessException;
import Model.ModelPaciente;
import View.Pacientes;
import java.util.List;
import java.util.stream.Collectors;

public class PacienteController {
    Pacientes view;
    ModelPaciente model;

    public PacienteController(Pacientes view, ModelPaciente model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();

    }

    private void cargarDatosIniciales() {
        try {
            List<Paciente> pacientes = Factory.get().paciente().obtenerTodos();
            model.setList(pacientes);
            model.setCurrent(new Paciente());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Paciente paciente) throws DataAccessException {
        try {
            if(Factory.get().paciente().existeId(paciente.getId())) {
                Factory.get().paciente().actualizar(paciente);
            } else {
                Factory.get().paciente().insertar(paciente);
            }
            model.setCurrent(new Paciente());
            model.setList(Factory.get().paciente().obtenerTodos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar el paciente" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            Paciente encontrado =  Factory.get().paciente().obtenerPorId(id);
            if (encontrado == null) {
                throw new DataAccessException("Paciente no encontrado");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            Paciente m = new Paciente();
            m.setId(id);
            model.setCurrent(m);
            throw ex;
        }
    }

    public void delete(String id) throws DataAccessException {
        Factory.get().paciente().eliminar(id);
        model.setCurrent(new Paciente());
        model.setList(Factory.get().paciente().obtenerTodos());
    }

    public void search(String search) throws DataAccessException {
        List<Paciente> general = Factory.get().paciente().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Paciente> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void clear() {
        model.setCurrent(new Paciente());
        model.setList(Factory.get().paciente().obtenerTodos());
    }
}