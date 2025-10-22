package hospital.Presentation.Login;

import Logic.Service;
import Logic.Entities.UsuarioBase;
import Logic.Exceptions.DataAccessException;

import java.util.List;
import java.util.stream.Collectors;

public class UsuariosController {
    Login view;
    ModelUsuarios model;

    public UsuariosController(Login view, ModelUsuarios model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        try {
            // CAMBIO: Factory → Service
            List<UsuarioBase> usuarios = Service.getInstance().findAllUsuarios();
            model.setList(usuarios);
            model.setCurrent(new UsuarioBase());
        } catch (Exception e) {
            System.err.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(UsuarioBase usuario) throws DataAccessException {
        try {
            if (usuario == null) {
                throw new DataAccessException("Usuario no puede ser null");
            }

            // CAMBIO: Usa Service
            // Nota: Service no tiene método exists para usuarios,
            // así que intentamos leer primero
            try {
                Service.getInstance().readUsuario(usuario.getId());
                // Si existe, actualizamos
                Service.getInstance().update(usuario);
            } catch (Exception e) {
                // Si no existe, creamos
                Service.getInstance().create(usuario);
            }

            model.setCurrent(new UsuarioBase());
            model.setList(Service.getInstance().findAllUsuarios());

        } catch (Exception x) {
            throw new DataAccessException("Error al guardar el usuario: " + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new DataAccessException("ID no válido");
            }

            // CAMBIO: Usa Service
            UsuarioBase encontrado = Service.getInstance().readUsuario(id);

            if (encontrado == null) {
                throw new DataAccessException("Usuario no encontrado");
            }

            model.setCurrent(encontrado);

        } catch (Exception ex) {
            UsuarioBase temp = new UsuarioBase();
            temp.setId(id);
            model.setCurrent(temp);
            throw new DataAccessException("Usuario no encontrado: " + ex.getMessage());
        }
    }

    public void delete(String id) throws DataAccessException {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new DataAccessException("ID no válido");
            }

            // CAMBIO: Service requiere objeto completo
            UsuarioBase usuario = new UsuarioBase();
            usuario.setId(id);
            Service.getInstance().delete(usuario);

            model.setCurrent(new UsuarioBase());
            model.setList(Service.getInstance().findAllUsuarios());

        } catch (Exception e) {
            throw new DataAccessException("Error al eliminar usuario: " + e.getMessage());
        }
    }

    public void search(String search) throws DataAccessException {
        try {
            // CAMBIO: Usa Service
            List<UsuarioBase> general = Service.getInstance().findAllUsuarios();

            if (search == null || search.trim().isEmpty()) {
                model.setList(general);
            } else {
                List<UsuarioBase> filtro = general.stream()
                        .filter(u -> u.getNombre() != null &&
                                u.getNombre().toLowerCase().contains(search.toLowerCase()))
                        .collect(Collectors.toList());
                model.setList(filtro);
            }
        } catch (Exception e) {
            throw new DataAccessException("Error en búsqueda: " + e.getMessage());
        }
    }

    public void clear() {
        try {
            model.setCurrent(new UsuarioBase());
            model.setList(Service.getInstance().findAllUsuarios());
        } catch (Exception e) {
            System.err.println("Error al limpiar: " + e.getMessage());
        }
    }

    /**
     * Método adicional para autenticación
     */
    public UsuarioBase authenticate(String id, String clave) throws DataAccessException {
        try {
            if (id == null || id.trim().isEmpty() || clave == null || clave.trim().isEmpty()) {
                throw new DataAccessException("Credenciales inválidas");
            }

            return Service.getInstance().authenticate(id, clave);

        } catch (Exception e) {
            throw new DataAccessException("Error de autenticación: " + e.getMessage());
        }
    }

    /**
     * Obtiene usuarios activos
     */
    public List<UsuarioBase> getUsuariosActivos() throws DataAccessException {
        try {
            return Service.getInstance().findUsuariosActivos();
        } catch (Exception e) {
            throw new DataAccessException("Error obteniendo usuarios activos: " + e.getMessage());
        }
    }

    /**
     * Cambia el estado activo de un usuario
     */
    public void setUsuarioActivo(String id, boolean activo) throws DataAccessException {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new DataAccessException("ID no válido");
            }

            Service.getInstance().setUsuarioActivo(id, activo);
            model.setList(Service.getInstance().findAllUsuarios());

        } catch (Exception e) {
            throw new DataAccessException("Error cambiando estado: " + e.getMessage());
        }
    }
}