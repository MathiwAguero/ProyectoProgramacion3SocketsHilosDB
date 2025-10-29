package hospital.Presentation.Prescripcion;

import hospital.Logic.Service;
import hospital.Logic.Exceptions.*;
import hospital.Entities.Entities.*;
import hospital.Logic.SocketListener;
import hospital.Presentation.Detalles.ModelDetails;
import hospital.Presentation.ThreadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescribirController implements ThreadListener {
    PrescribirMed view;
    ModelDetails model;
    SocketListener socketListener;
    public PrescribirController(PrescribirMed view, ModelDetails model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();
        try {
            socketListener = new SocketListener(this, ((Service)Service.getInstance()).getSid());
            socketListener.start();
            System.out.println("✓ SocketListener iniciado en DetailsController");
        } catch (Exception e) {
            System.err.println("✗ Error iniciando SocketListener: " + e.getMessage());
        }
    }

    private void cargarDatosIniciales() {
        try {
            // Inicia con lista vacía
            model.setList(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    /**
     * Crea o actualiza una receta completa con sus detalles
     */
    public Receta create(Receta receta) throws DataAccessException {
        try {
            if (receta == null) {
                throw new DataAccessException("Receta no puede ser null");
            }

            // CAMBIO: Usa Service
            if (Service.getInstance().existsReceta(receta.getId())) {
                Service.getInstance().update(receta);
            } else {
                Service.getInstance().create(receta);
            }

            // Limpia la lista después de guardar
            model.setList(new ArrayList<>());

            return receta;

        } catch (Exception x) {
            throw new DataAccessException("Error al guardar la receta: " + x.getMessage());
        }
    }

    /**
     * Lee una receta y carga sus detalles
     */
    public void read(String id) throws DataAccessException {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new DataAccessException("ID de receta no válido");
            }

            // CAMBIO: Usa Service
            Receta receta = Service.getInstance().readReceta(id);

            if (receta == null) {
                throw new DataAccessException("Receta no encontrada");
            }

            // Carga los detalles en el modelo
            model.setList(receta.getDetalles() != null ? receta.getDetalles() : new ArrayList<>());

        } catch (Exception ex) {
            model.setList(new ArrayList<>());
            throw new DataAccessException("Receta no encontrada: " + ex.getMessage());
        }
    }

    /**
     * Elimina una receta
     */
    public void delete(String id) throws DataAccessException {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new DataAccessException("ID de receta no válido");
            }

            // CAMBIO: Service requiere objeto completo
            Receta receta = new Receta();
            receta.setId(id);
            Service.getInstance().delete(receta);

            model.setList(new ArrayList<>());

        } catch (Exception e) {
            throw new DataAccessException("Error al eliminar receta: " + e.getMessage());
        }
    }

    /**
     * Busca una receta por ID y carga sus detalles
     */
    public void search(String search) throws DataAccessException {
        try {
            if (search == null || search.trim().isEmpty()) {
                model.setList(new ArrayList<>());
                return;
            }

            // CAMBIO: Usa Service
            List<Receta> general = Service.getInstance().findAllRecetas();

            // Filtra por ID exacto
            List<Receta> filtro = general.stream()
                    .filter(r -> r.getId() != null && r.getId().equals(search))
                    .collect(Collectors.toList());

            if (filtro.isEmpty()) {
                model.setList(new ArrayList<>());
                throw new DataAccessException("No se encontró receta con ID: " + search);
            }

            // Toma la primera (debería ser única)
            Receta encontrada = filtro.get(0);
            model.setList(encontrada.getDetalles() != null ? encontrada.getDetalles() : new ArrayList<>());

        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error en búsqueda: " + e.getMessage());
        }
    }

    /**
     * Limpia el formulario
     */
    public void clear() {
        try {
            model.setList(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error al limpiar: " + e.getMessage());
        }
    }

    /**
     * Valida que una receta tenga todos los datos necesarios
     */
    public boolean validarReceta(Receta receta) throws DataAccessException {
        if (receta == null) {
            throw new DataAccessException("Receta no puede ser null");
        }

        if (receta.getId() == null || receta.getId().trim().isEmpty()) {
            throw new DataAccessException("ID de receta es obligatorio");
        }

        if (receta.getPaciente() == null) {
            throw new DataAccessException("Debe seleccionar un paciente");
        }

        if (receta.getMedico() == null) {
            throw new DataAccessException("Debe seleccionar un médico");
        }

        if (receta.getDetalles() == null || receta.getDetalles().isEmpty()) {
            throw new DataAccessException("Debe agregar al menos un medicamento");
        }

        if (receta.getFechaConfeccion() == null || receta.getFechaConfeccion().trim().isEmpty()) {
            throw new DataAccessException("Fecha de confección es obligatoria");
        }

        return true;
    }

    @Override
    public void deliver_message(String message) {
        try {
            // Recargar todos los detalles cuando hay cambios
            List<RecipeDetails> detalles = Service.getInstance().findAllDetalles();
            model.setList(detalles);

            System.out.println("📩 Detalles refrescados: " + message);
            System.out.println("   Total detalles actualizados: " + detalles.size());

        } catch (Exception e) {
            System.err.println("✗ Error refrescando detalles: " + e.getMessage());
        }
    }

    /**
     * Detener el SocketListener al cerrar
     */
    public void stop() {
        if (socketListener != null) {
            socketListener.stop();
            System.out.println("✓ SocketListener detenido");
        }
    }
}