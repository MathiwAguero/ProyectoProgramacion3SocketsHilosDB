package hospital.Presentation.Recetas;

import Logic.Entities.EstadoReceta;
import Logic.Listas.Factory;
import Logic.Entities.Receta;
import Logic.Exceptions.DataAccessException;
import Logic.Service;
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
            List<Receta> recetas = Service.getInstance().findAllRecetas();
            model.setList(recetas);
            model.setCurrent(new Receta());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(Receta receta) throws Exception {
        try {
            if (Service.getInstance().existsReceta(receta.getId())) {
                Service.getInstance().update(receta);
            } else {
                Service.getInstance().create(receta);
            }
            model.setCurrent(new Receta());
            model.setList(Service.getInstance().findAllRecetas());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar la receta" + x.getMessage());
        }
    }

    public void read(String id) throws Exception {
        try {
            Receta encontrado = Service.getInstance().readReceta(id);
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

    public void delete(String id) throws Exception {
        Receta rec= new Receta();
        rec.setId(id);
        Service.getInstance().delete(rec);
        model.setCurrent(new Receta());
        model.setList(Service.getInstance().findAllRecetas());
    }

    public void search(String search) throws Exception {
        List<Receta> general = Service.getInstance().findAllRecetas();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<Receta> filtro = general.stream()
                    .filter(m -> m.getId() != null && m.getId().equals(search))
                    .collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void clear() throws Exception {
        model.setCurrent(new Receta());
        model.setList(Service.getInstance().findAllRecetas());
    }

    public void actualizarEstado(String idReceta, EstadoReceta nuevo) throws Exception {
        if (idReceta == null) {
            throw new DataAccessException("ID de receta inválido.");
        }

        Receta rec = Service.getInstance().readReceta(idReceta);

        rec.setEstado(nuevo);
        Service.getInstance().update(rec);
        model.setCurrent(rec);
        model.setList(Service.getInstance().findAllRecetas());
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
