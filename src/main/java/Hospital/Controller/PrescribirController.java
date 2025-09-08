package Hospital.Controller;

import Hospital.ManejoListas.Factory;
import  Hospital.Entidades.Receta;
import  Hospital.Entidades.RecipeDetails;
import Hospital.Exceptions.DataAccessException;
import  Hospital.Model.ModelDetails;
import  Hospital.View.PrescribirMed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescribirController {
    PrescribirMed view;
    ModelDetails model;

    public PrescribirController(PrescribirMed view, ModelDetails model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        try {
            model.setList(new ArrayList<>());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Receta receta) throws DataAccessException {
        try {
            if (receta.getId() != null && Factory.get().receta().existeId(receta.getId())) {
                Factory.get().receta().actualizar(receta);
            } else {
                Factory.get().receta().insertar(receta);
            }
            model.setList(new ArrayList<>());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar la receta" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            Receta r = Factory.get().receta().obtenerPorId(id);
            if (r == null) throw new DataAccessException("Receta no encontrada");
            model.setList(r.getDetalles() != null ? r.getDetalles() : new ArrayList<>());
        } catch (DataAccessException ex) {
            model.setList(new ArrayList<>());
            throw ex;
        }
    }

    public void delete(String id) throws DataAccessException {
        Factory.get().receta().eliminar(id);
        model.setList(new ArrayList<>());
    }

    public void search(String search) throws DataAccessException {
        List<Receta> general = Factory.get().receta().obtenerTodos();
        if (search == null) {
            model.setList(new ArrayList<>());
        } else {
            List<Receta> filtro = general.stream() .filter(r -> r.getId() != null && r.getId().equals(search))
                    .collect(Collectors.toList());
                Receta nueva = filtro.get(0);
                model.setList(nueva.getDetalles());
        }
    }

    public void clear() {
        model.setList(new ArrayList<>());
    }
}
