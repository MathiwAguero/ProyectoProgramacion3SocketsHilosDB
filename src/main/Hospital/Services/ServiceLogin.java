package Services;

import DataAccessObject.DAOFactory;
import DataAccessObject.UsuarioDAO;
import Exceptions.DataAccessException;
import Model.TipoUsuario;
import Model.UsuarioBase;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceLogin {
    private final UsuarioDAO usuarioDAO = DAOFactory.get().usuario();

    //Esta es la autenticacion de usuario para que cuando se ingrese no confunda el tipo
    public UsuarioBase login(String id, String clave, String nombre, TipoUsuario type) throws DataAccessException {
        if (id == null || clave == null || type == null) {
            throw new DataAccessException("Datos incompletos");
        }

        UsuarioBase aux = usuarioDAO.obtenerPorId(id);
        if (aux == null) {
            throw new DataAccessException("Usuario no existe");
        }

        if (!clave.equals(aux.getClave())) {
            throw new DataAccessException("Clave incorrecta");
        }

        if (aux.getTipo() != type) {
            throw new DataAccessException("Tipo de usuario incorrecto");
        }

        return aux;
    }

    //Metodo para el cambio de contrasena
    public void cambioClave(String id, String claveNueva) throws DataAccessException {
        if (id == null || claveNueva == null) throw new DataAccessException("Dato de clave incompleto");
        UsuarioBase temp = usuarioDAO.obtenerPorId(id);
        if (temp == null) {
            throw new DataAccessException("Usuario no existe");
        }
        temp.setClave(claveNueva);
        usuarioDAO.actualizar(temp);
    }

    public List<UsuarioBase> obtenerTipo(TipoUsuario tipo) {
        return usuarioDAO.obtenerTodos().stream().filter(u -> u.getTipo() == tipo).collect(Collectors.toList());
    }

}
