package hospital.Presentation.Detalles;

import hospital.Logic.Service;
import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.DataAccessException;
import hospital.Logic.SocketListener;
import hospital.Presentation.ThreadListener;

import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class DetailsController implements ThreadListener{
    ModelDetails model;
    SocketListener socketListener;
    public DetailsController(ModelDetails model) {
        this.model = model;
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
            // CAMBIO: Factory → Service
            List<RecipeDetails> details = Service.getInstance().findAllDetalles();
            model.setList(details);
            model.setCurrent(new RecipeDetails());
        } catch (Exception e) {
            System.err.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    /**
     * Crea o actualiza un detalle de receta
     * NOTA: Los detalles normalmente se crean asociados a una receta específica
     */
    public void create(RecipeDetails details, String recetaId) throws DataAccessException {
        try {
            if (details == null) {
                throw new DataAccessException("Detalle no puede ser null");
            }

            if (recetaId == null || recetaId.trim().isEmpty()) {
                throw new DataAccessException("ID de receta no válido");
            }

            // CAMBIO: Usa Service para crear detalle
            Service.getInstance().createDetalle(details, recetaId);

            model.setCurrent(new RecipeDetails());
            model.setList(Service.getInstance().findAllDetalles());

        } catch (Exception x) {
            throw new DataAccessException("Error con la prescripción de detalles: " + x.getMessage());
        }
    }

    /**
     * Lee los detalles de una receta específica
     */
    public void read(String recetaId) throws DataAccessException {
        try {
            if (recetaId == null || recetaId.trim().isEmpty()) {
                throw new DataAccessException("ID de receta no válido");
            }

            // CAMBIO: Usa Service para obtener detalles por receta
            List<RecipeDetails> encontrados = Service.getInstance().findDetallesByReceta(recetaId);

            if (encontrados == null || encontrados.isEmpty()) {
                throw new DataAccessException("No se encontraron detalles");
            }

            model.setList(encontrados);
            model.setCurrent(encontrados.get(0));

        } catch (Exception ex) {
            throw new DataAccessException("Detalles no encontrados: " + ex.getMessage());
        }
    }

    /**
     * Elimina detalles de una receta
     * NOTA: Normalmente los detalles se eliminan al eliminar la receta
     */
    public void delete(String recetaId) throws DataAccessException {
        try {
            if (recetaId == null || recetaId.trim().isEmpty()) {
                throw new DataAccessException("ID de receta no válido");
            }

            // Los detalles se eliminan en cascada al eliminar la receta
            // No hay método directo en Service para esto
            // Si necesitas eliminar detalles individuales, agrégalo al Service

            model.setCurrent(new RecipeDetails());
            model.setList(Service.getInstance().findAllDetalles());

        } catch (Exception e) {
            throw new DataAccessException("Error al eliminar detalles: " + e.getMessage());
        }
    }

    /**
     * Busca detalles por código de medicamento
     */
    public void search(String search) throws DataAccessException {
        try {
            // CAMBIO: Usa Service
            List<RecipeDetails> general = Service.getInstance().findAllDetalles();

            if (search == null || search.trim().isEmpty()) {
                model.setList(general);
            } else {
                List<RecipeDetails> filtro = general.stream()
                        .filter(d -> d.getCodigoMedicamento() != null &&
                                d.getCodigoMedicamento().toLowerCase().contains(search.toLowerCase()))
                        .collect(Collectors.toList());
                model.setList(filtro);
            }
        } catch (Exception e) {
            throw new DataAccessException("Error en búsqueda: " + e.getMessage());
        }
    }

    public void clear() {
        try {
            model.setCurrent(new RecipeDetails());
            model.setList(Service.getInstance().findAllDetalles());
        } catch (Exception e) {
            System.err.println("Error al limpiar: " + e.getMessage());
        }
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