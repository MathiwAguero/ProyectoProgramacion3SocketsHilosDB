package Controller;

import Entidades.UsuarioBase;
import ManejoListas.Factory;
import Entidades.Medico;
import Exceptions.DataAccessException;
import Model.ModelMedico;
import Model.ModelUsuarios;
import View.Login;
import View.Medicos;
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
            List<UsuarioBase> usuarios = Factory.get().usuario().obtenerTodos();
            model.setList(usuarios);
            model.setCurrent(new UsuarioBase());
        } catch (Exception e) {
            System.out.println("Error cargando datos iniciales: " + e.getMessage());
        }
    }

    public void create(UsuarioBase usuario) throws DataAccessException {
        try {
            if(Factory.get().usuario().existeId(usuario.getId())) {
                Factory.get().usuario().actualizar(usuario);
            } else {
                Factory.get().usuario().insertar(usuario);
            }
            model.setCurrent(new UsuarioBase());
            model.setList(Factory.get().usuario().obtenerTodos());
        } catch (DataAccessException x) {
            throw new DataAccessException("Error al guardar el usuario" + x.getMessage());
        }
    }

    public void read(String id) throws DataAccessException {
        try {
            UsuarioBase encontrado =  Factory.get().usuario().obtenerPorId(id);
            if (encontrado == null) {
                throw new DataAccessException("Usuario no encontrado");
            }
            model.setCurrent(encontrado);
        } catch (DataAccessException ex) {
            UsuarioBase m = new UsuarioBase();
            m.setId(id);
            model.setCurrent(m);
            throw ex;
        }
    }

    public void delete(String id) throws DataAccessException {
        Factory.get().usuario().eliminar(id);
        model.setCurrent(new UsuarioBase());
        model.setList(Factory.get().usuario().obtenerTodos());
    }

    public void search(String search) throws DataAccessException {
        List<UsuarioBase> general = Factory.get().usuario().obtenerTodos();
        if (search == null || search.trim().isEmpty()) {
            model.setList(general);
        } else {
            List<UsuarioBase> filtro = general.stream().filter(m -> m.getNombre() != null && m.getNombre().toLowerCase()
                    .contains(search.toLowerCase())).collect(Collectors.toList());
            model.setList(filtro);
        }
    }

    public void clear() {
        model.setCurrent(new UsuarioBase());
        model.setList(Factory.get().usuario().obtenerTodos());
    }
}