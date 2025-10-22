package hospital.Presentation.Pacientes;

import Logic.Listas.Factory;
import Logic.Entities.Paciente;
import Logic.Exceptions.DataAccessException;
import Logic.Service;
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
            List<Paciente> pacientes = Service.getInstance().findAllPacientes();
            model.setList(pacientes);
            model.setCurrent(new Paciente());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Paciente paciente) throws Exception {
        try {
            if(Service.getInstance().existsPaciente(paciente.getId())) {
                Service.getInstance().update(paciente);
            } else {
                Service.getInstance().create(paciente);
            }
            model.setCurrent(new Paciente());
            model.setList(Service.getInstance().findAllPacientes());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar el paciente" + x.getMessage());
        }
    }

    public void read(String id) throws Exception {
        try {
            Paciente encontrado =  Service.getInstance().readPaciente(id);
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

    public void delete(String id) throws Exception {
        Paciente paciente= new Paciente();
        paciente.setId(id);
        Service.getInstance().delete(paciente);
        model.setCurrent(new Paciente());
        model.setList(Service.getInstance().findAllPacientes());
    }

    public void search(String search) throws Exception {
        List<Paciente> general = Service.getInstance().findAllPacientes();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Paciente> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void searchComboBox(String criterio, String search) throws Exception {
        List<Paciente> general = Service.getInstance().findAllPacientes();
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

    public void clear() throws Exception {
        model.setCurrent(new Paciente());
        model.setList(Service.getInstance().findAllPacientes());
    }
}