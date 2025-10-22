package hospital.Presentation.Medicamentos;

import Logic.Listas.Factory;
import Logic.Entities.Medicamento;
import Logic.Exceptions.DataAccessException;
import Logic.Service;
import Presentation.Medicamentos.ModelMedicamentos;
import Presentation.Medicamentos.Medicamentos;
import Presentation.Medicamentos.MedicamentoController;

import Presentation.Prescripcion.Filtros.PrescribirBuscarMedica;

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
            List<Medicamento> medicamentos = Service.getInstance().findAllMedicamentos();
            model.setList(medicamentos);
            model.setCurrent(new Medicamento());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Medicamento medicamento) throws DataAccessException {
        try {
            if(Service.getInstance().existsMedicamento(medicamento.getCodigo())) {
                Service.getInstance().update(medicamento);
            } else {
                Service.getInstance().create(medicamento);
            }
            model.setCurrent(new Medicamento());
            model.setList(Service.getInstance().findAllMedicamentos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar el medicamento" + x.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void read(String id) throws Exception {
        try {
            Medicamento encontrado = Service.getInstance().readMedicamento(id);
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

    public void delete(String id) throws Exception {
        Medicamento med=new Medicamento();
        med.setCodigo(id);
        Service.getInstance().delete(med);
        model.setCurrent(new Medicamento());
        model.setList(Service.getInstance().findAllMedicamentos());
    }

    public void search(String search) throws Exception {
        List<Medicamento> general = Service.getInstance().findAllMedicamentos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Medicamento> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void searchComboBox(String criterio, String search) throws Exception {
        List<Medicamento> general = Service.getInstance().findAllMedicamentos();
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

    public void clear() throws Exception {
        model.setCurrent(new Medicamento());
        model.setList(Service.getInstance().findAllMedicamentos());
    }
}