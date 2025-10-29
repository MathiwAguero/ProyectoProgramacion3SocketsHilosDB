package hospital.Presentation.Medicos;

import hospital.Logic.Listas.Factory;
import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.DataAccessException;
import hospital.Logic.Service;
import hospital.Logic.SocketListener;
import hospital.Presentation.Medicos.MedicoController;
import hospital.Presentation.ThreadListener;

import java.util.List;
import java.util.stream.Collectors;

public class MedicoController implements ThreadListener {
    Medicos view;
    ModelMedico model;
    SocketListener socketListener;
    public MedicoController(Medicos view, ModelMedico model) {
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
            if(Service.getInstance().existsMedico(medico.getId())) {
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

    @Override
    public void deliver_message(String message) {
        try { search(new Medico().getId()); } catch (Exception e) { }
        System.out.println(message);
    }
}