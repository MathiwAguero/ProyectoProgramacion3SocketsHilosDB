package Presentation.Medicos;

import Logic.Listas.Factory;
import Logic.Entities.Medico;
import Logic.Exceptions.DataAccessException;
import Logic.Service;
import Presentation.Medicos.MedicoController;

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
            List<Medico> medicos = Service.getInstance().findAllMedicos();
            model.setList(medicos);
            model.setCurrent(new Medico());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Medico medico) throws DataAccessException {
        try {
            if(Service.getInstance().existsFarmaceuta(medico.getId())) {
                Service.getInstance().update(medico);
            } else {
                Service.getInstance().create(medico);
            }
            model.setCurrent(new Medico());
            model.setList(Service.getInstance().findAllMedicos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar el medico" + x.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            Medico encontrado =  Service.getInstance().readMedico(id);
            if (encontrado == null) {
                throw new DataAccessException("Medico no encontrado");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            Medico m = new Medico();
            m.setId(id);
            model.setCurrent(m);
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) throws Exception {
        Medico med= new Medico();
        med.setId(id);
        Service.getInstance().delete(med);
        model.setCurrent(new Medico());
        model.setList(Service.getInstance().findAllMedicos());
    }

    public void search(String search) throws Exception {
        List<Medico> general = Service.getInstance().findAllMedicos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Medico> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }


    public void clear() throws Exception {
        model.setCurrent(new Medico());
        model.setList(Service.getInstance().findAllMedicos());
    }
}