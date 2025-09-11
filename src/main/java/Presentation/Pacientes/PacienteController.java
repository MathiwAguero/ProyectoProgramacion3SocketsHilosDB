package Presentation.Pacientes;

import Logic.Listas.Factory;
import Logic.Entities.Paciente;
import Logic.Exceptions.DataAccessException;
import Presentation.Prescripcion.Filtros.PrescribirBuscarPacien;


import java.util.List;
import java.util.stream.Collectors;

public class PacienteController {
    Pacientes panelview;
    ModelPaciente model;
    PrescribirBuscarPacien panel;

    public PacienteController(Pacientes panelview, ModelPaciente model) {
        this.panelview = panelview;
        this.model = model;
        panelview.setController(this);
        panelview.setModel(model);
        cargarDatosIniciales();

    }
    public PacienteController(PrescribirBuscarPacien panel, ModelPaciente model) {
        this.panel = panel;
        this.model = model;
        panel.setController(this);
        panel.setModel(model);
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

    public void searchComboBox(String criterio, String search) throws DataAccessException {
        List<Paciente> general = Factory.get().paciente().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
            return;
        }
        String q = search.toLowerCase();

        if ("ID".equalsIgnoreCase(criterio)) {
            model.setList(general.stream()
                    .filter(p -> p.getId() != null && p.getId().toLowerCase().startsWith(q))
                    .collect(Collectors.toList()));
        } else {
            model.setList(general.stream()
                    .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(q))
                    .collect(Collectors.toList()));
        }
    }

    public void clear() {
        model.setCurrent(new Paciente());
        model.setList(Factory.get().paciente().obtenerTodos());
    }
}