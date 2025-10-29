package hospital.Presentation.Medicamentos;

import hospital.Logic.Listas.Factory;
import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.DataAccessException;
import hospital.Logic.Service;
import hospital.Logic.SocketListener;
import hospital.Presentation.Medicamentos.ModelMedicamentos;
import hospital.Presentation.Medicamentos.Medicamentos;
import hospital.Presentation.Medicamentos.MedicamentoController;

import hospital.Presentation.Prescripcion.Filtros.PrescribirBuscarMedica;
import hospital.Presentation.ThreadListener;

import java.util.List;
import java.util.stream.Collectors;

public class MedicamentoController implements ThreadListener {
    Medicamentos view;
    ModelMedicamentos model;
    PrescribirBuscarMedica panel;
    SocketListener socketListener;

    public MedicamentoController(Medicamentos view, ModelMedicamentos model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();

        try {
            socketListener = new SocketListener(this, ((Service)Service.getInstance()).getSid());
            socketListener.start();
        } catch (Exception e) {}
    }

    public MedicamentoController(PrescribirBuscarMedica view, ModelMedicamentos model) {
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();
        try {
            socketListener = new SocketListener(this, ((Service)Service.getInstance()).getSid());
            socketListener.start();
        } catch (Exception e) {}

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

    @Override
    public void deliver_message(String message) {
        try { search(new Medicamento().getCodigo()); } catch (Exception e) { }
        System.out.println(message);
    }
}