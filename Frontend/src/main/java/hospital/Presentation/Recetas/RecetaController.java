package hospital.Presentation.Recetas;

import hospital.Entities.Entities.*;

import hospital.Logic.Exceptions.DataAccessException;
import hospital.Logic.NotificationManager;
import hospital.Logic.SocketListener;
import hospital.Presentation.Dashboard.Dashboard;
import hospital.Presentation.Despacho.Despacho;
import com.toedter.calendar.JDateChooser;
import hospital.Logic.Service;
import hospital.Presentation.ThreadListener;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RecetaController implements ThreadListener {
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
        NotificationManager.getInstance().register(this);
    }

    // Constructor usado por Historial
    public RecetaController(Historial viewHistorial, ModelReceta model) {
        this.viewHistorial = viewHistorial;
        this.model = model;
        viewHistorial.setController(this);
        viewHistorial.setModel(model);
        cargarDatosIniciales();
        NotificationManager.getInstance().register(this);
    }

    //Constructor usado por despacho
    public RecetaController(Despacho viewDespacho, ModelReceta model) {
        this.viewDespacho = viewDespacho;
        this.model = model;
        viewDespacho.setController(this);
        viewDespacho.setModel(model);
        cargarDatosIniciales();
        NotificationManager.getInstance().register(this);
    }


    private void cargarDatosIniciales() {
        try {
            List<Receta> recetas = Service.instance().findAllRecetas();
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
            Receta encontrado = Service.instance().readReceta(id);
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
        Receta rec = new Receta();
        rec.setId(id);
        Service.getInstance().delete(rec);
        model.setCurrent(new Receta());
        model.setList(Service.instance().findAllRecetas());
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
        model.setList(Service.instance().findAllRecetas());
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

        List<Receta> recetas = null;
        try {
            recetas = Service.getInstance().findAllRecetas();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
            String fechaStr = (String) fechaObj;

            // ✓ CAMBIO: Intentar primero formato SQL, luego formato display
            SimpleDateFormat[] formatos = {
                    new SimpleDateFormat("yyyy-MM-dd"),      // SQL format (nuevo)
                    new SimpleDateFormat("dd/MM/yyyy"),      // Display format (viejo)
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            };

            for (SimpleDateFormat formato : formatos) {
                try {
                    return formato.parse(fechaStr);
                } catch (ParseException e) {
                    // Intentar siguiente formato
                }
            }

            System.err.println("No se pudo parsear la fecha: " + fechaStr);
            return null;

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

    @Override
    public void deliver_message(String message) {
        try {
            System.out.println("[DEBUG] Mensaje recibido: " + message);
            System.out.println("[DEBUG] Lista antes de actualizar: " + model.getList().size());
            search(null);
            System.out.println("[DEBUG] Lista después de actualizar: " + model.getList().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cleanup() {
        NotificationManager.getInstance().unregister(this);
        System.out.println("✓ MedicoController desregistrado");
    }
}