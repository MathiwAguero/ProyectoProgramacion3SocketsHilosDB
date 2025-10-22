package hospital.Presentation.Farmaceutas;

import Logic.Listas.Factory;
import Logic.Entities.Farmaceuta;
import Logic.Exceptions.DataAccessException;
import Logic.Service;

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
            List<Farmaceuta> farmaceutas = Service.getInstance().findAllFarmaceutas();
            model.setList(farmaceutas);
            model.setCurrent(new Farmaceuta());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Farmaceuta farmaceuta) throws DataAccessException {
        try {
            if(Service.getInstance().existsFarmaceuta(farmaceuta.getId())) {
                Service.getInstance().update(farmaceuta);
            } else {
                Service.getInstance().create(farmaceuta);
            }
            model.setCurrent(new Farmaceuta());
            model.setList(Service.getInstance().findAllFarmaceutas());
        } catch (Exception x) {
            throw new DataAccessException("Error al guardar el farmaceuta" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            Farmaceuta encontrado =  Service.getInstance().readFarmaceuta(id);
            if (encontrado == null) {
                throw new DataAccessException("Medico no encontrado");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            Farmaceuta m = new Farmaceuta();
            m.setId(id);
            model.setCurrent(m);
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Farmaceuta id) throws Exception {
       Service.getInstance().delete(id);
        model.setCurrent(new Farmaceuta());
        model.setList(Service.getInstance().findAllFarmaceutas());
    }

    public void search(String search) throws Exception {
        List<Farmaceuta> general = Service.getInstance().findAllFarmaceutas();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Farmaceuta> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void clear() throws Exception {
        model.setCurrent(new Farmaceuta());
        model.setList(Service.getInstance().findAllFarmaceutas());
    }
}