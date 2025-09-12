package Presentation.Recetas;

import Logic.Entities.EstadoReceta;
import Logic.Listas.Factory;
import Logic.Entities.Receta;
import Logic.Exceptions.DataAccessException;
import Presentation.Dashboard.Dashboard;
import Presentation.Despacho.Despacho;
import com.toedter.calendar.JDateChooser;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RecetaController {
    Dashboard viewDashboard;
    Historial viewHistorial;
    ModelReceta model;
    Despacho viewDespacho;

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

    //Constructor usado por despacho
    public RecetaController(Despacho viewDespacho, ModelReceta model) {
        this.viewDespacho = viewDespacho;
        this.model = model;
        viewDespacho.setController(this);
        viewDespacho.setModel(model);
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

    public void actualizarEstado(String idReceta, EstadoReceta nuevo) throws DataAccessException {
        if (idReceta == null) {
            throw new DataAccessException("ID de receta inválido.");
        }

        Receta rec = Factory.get().receta().obtenerPorId(idReceta);

        rec.setEstado(nuevo);
        Factory.get().receta().actualizar(rec);
        model.setCurrent(rec);
        model.setList(Factory.get().receta().obtenerTodos());
    }
    public List<Receta> FiltradasPorNombre(String nombre, List<Receta> recetas) throws DataAccessException {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return recetas.stream()
                .filter(receta -> receta.getDetalles().stream()
                        .anyMatch(d -> d.getNombre() != null &&
                                d.getNombre().equalsIgnoreCase(nombre)))
                .toList();
    }

    public List<Receta> RecetasPorFecha(JDateChooser Desde, JDateChooser Hasta) {
        if (Desde == null || Hasta == null) return new ArrayList<>();

        Date fechaDesde = Desde.getDate();
        Date fechaHasta = Hasta.getDate();
        if (fechaDesde == null || fechaHasta == null) return new ArrayList<>();

        if (fechaHasta.compareTo(fechaDesde) < 0) return new ArrayList<>();

        List<Receta> recetas = model.getList();

        List<Receta> filtro = recetas.stream()
                .filter(x -> x.getFechaConfeccion() != null) // Solo recetas con fecha de entrega
                .filter(x -> {
                    try {
                        Date fechaEntrega = parsearFecha(x.getFechaConfeccion());

                        return fechaEntrega != null &&
                                !fechaEntrega.before(fechaDesde) &&
                                !fechaEntrega.after(fechaHasta);

                    } catch (Exception e) {
                        System.err.println("Error procesando fecha entrega para receta " + x.getId() + ": " + e.getMessage());
                        return false;
                    }
                })
                .collect(Collectors.toList());

        return filtro;
    }

    private Date parsearFecha(Object fechaObj) {
        if (fechaObj == null) return null;

        if (fechaObj instanceof Date) {
            return (Date) fechaObj;
        } else if (fechaObj instanceof String) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.parse((String) fechaObj);
            } catch (ParseException e) {
                System.err.println("Error parseando fecha string: " + fechaObj);
                return null;
            }
        } else if (fechaObj instanceof JDateChooser) {
            return ((JDateChooser) fechaObj).getDate();
        }

        try {
            Method getDateMethod = fechaObj.getClass().getMethod("getDate");
            return (Date) getDateMethod.invoke(fechaObj);
        } catch (Exception e) {
            System.err.println("No se pudo obtener fecha de: " + fechaObj.getClass());
            return null;
        }
    }
}
