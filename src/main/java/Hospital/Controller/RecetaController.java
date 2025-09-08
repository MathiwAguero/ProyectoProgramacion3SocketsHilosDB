package Hospital.Controller;

import Hospital.ManejoListas.Factory;
import  Hospital.Entidades.Receta;
import Hospital.Exceptions.DataAccessException;
import  Hospital.Model.ModelReceta;
import  Hospital.View.Dashboard;
import  Hospital.View.Historial;

import java.util.List;
import java.util.stream.Collectors;

public class RecetaController {
    Dashboard viewDashboard;
    Historial viewHistorial;
    ModelReceta model;

    // Constructor usado por Dashboard
    public RecetaController(Dashboard viewDashboard, ModelReceta model) {
        this.viewDashboard = viewDashboard;
        this.model = model;
        viewDashboard.setController(this);
        viewDashboard.setModel(model);
        cargarDatosIniciales();
    }

    // Constructor usado por Historial
    public RecetaController(Historial viewHistorial, ModelReceta model) {
        this.viewHistorial = viewHistorial;
        this.model = model;
        viewHistorial.setController(this);
        viewHistorial.setModel(model);
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        try {
            List<Receta> recetas = Factory.get().receta().obtenerTodos();
            model.setList(recetas);
            model.setCurrent(new Receta());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Receta receta) throws DataAccessException {
        try {
            if (Factory.get().receta().existeId(receta.getId())) {
                Factory.get().receta().actualizar(receta);
            } else {
                Factory.get().receta().insertar(receta);
            }
            model.setCurrent(new Receta());
            model.setList(Factory.get().receta().obtenerTodos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar la receta" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            Receta encontrado = Factory.get().receta().obtenerPorId(id);
            if (encontrado == null) {
                throw new DataAccessException("Receta no encontrada");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            Receta m = new Receta();
            m.setId(id);
            model.setCurrent(m);
            throw ex;
        }
    }

    public void delete(String id) throws DataAccessException {
        Factory.get().receta().eliminar(id);
        model.setCurrent(new Receta());
        model.setList(Factory.get().receta().obtenerTodos());
    }

    public void search(String search) throws DataAccessException {
        List<Receta> general = Factory.get().receta().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Receta> filtro = general.stream()
                    .filter(m -> m.getId() != null && m.getId().equals(search))
                    .collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void clear() {
        model.setCurrent(new Receta());
        model.setList(Factory.get().receta().obtenerTodos());
    }
}
