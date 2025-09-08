package Hospital.Controller;

import  Hospital.Entidades.Medico;
import Hospital.ManejoListas.Factory;
import  Hospital.Entidades.Medicamento;
import Hospital.Exceptions.DataAccessException;
import  Hospital.Model.ModelMedicamentos;
import  Hospital.View.Medicamentos;
import  Hospital.View.PrescribirBuscarMedica;

import java.util.List;
import java.util.stream.Collectors;

public class MedicamentoController {
    Medicamentos view;
    ModelMedicamentos model;
    PrescribirBuscarMedica panel;


    public MedicamentoController(Medicamentos view, ModelMedicamentos model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();

    }

    public MedicamentoController(PrescribirBuscarMedica view, ModelMedicamentos model) {
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        try {
            List<Medicamento> medicamentos = Factory.get().medicamento().obtenerTodos();
            model.setList(medicamentos);
            model.setCurrent(new Medicamento());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Medicamento medicamento) throws DataAccessException {
        try {
            if(Factory.get().medicamento().existeId(medicamento.getCodigo())) {
                Factory.get().medicamento().actualizar(medicamento);
            } else {
                Factory.get().medicamento().insertar(medicamento);
            }
            model.setCurrent(new Medicamento());
            model.setList(Factory.get().medicamento().obtenerTodos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar el medicamento" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            Medicamento encontrado =  Factory.get().medicamento().obtenerPorId(id);
            if (encontrado == null) {
                throw new DataAccessException("Medicamento no encontrado");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            Medicamento m = new Medicamento();
            m.setCodigo(id);
            model.setCurrent(m);
            throw ex;
        }
    }

    public void delete(String id) throws DataAccessException {
        Factory.get().medicamento().eliminar(id);
        model.setCurrent(new Medicamento());
        model.setList(Factory.get().medicamento().obtenerTodos());
    }

    public void search(String search) throws DataAccessException {
        List<Medicamento> general = Factory.get().medicamento().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Medicamento> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void searchComboBox(String criterio, String search) throws DataAccessException {
        List<Medicamento> general = Factory.get().medicamento().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
            return;
        }
        String q = search.toLowerCase();

        if ("Código".equalsIgnoreCase(criterio)) {
            model.setList(general.stream()
                    .filter(p -> p.getCodigo() != null && p.getCodigo().toLowerCase().startsWith(q))
                    .collect(Collectors.toList()));
        } else {
            model.setList(general.stream()
                    .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(q))
                    .collect(Collectors.toList()));
        }
    }

    public void clear() {
        model.setCurrent(new Medicamento());
        model.setList(Factory.get().medicamento().obtenerTodos());
    }
}