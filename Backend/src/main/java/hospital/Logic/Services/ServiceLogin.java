package hospital.Logic.Services;

import hospital.Logic.Listas.*;
import hospital.Entities.Entities.*;
import hospital.Logic.Exceptions.*;
import hospital.Entities.Entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceLogin {
    private final ListUsers listUsers = Factory.get().usuario();

    // Login exacto por ID + clave
    public UsuarioBase loginPorId(String id, String clave) throws DataAccessException {
        if (id == null || id.isEmpty() || clave == null) {
            throw new DataAccessException("Datos incompletos");
        }
        UsuarioBase u = listUsers.obtenerPorId(id);
        if (u == null) throw new DataAccessException("Usuario no existe");
        if (!clave.equals(u.getClave())) throw new DataAccessException("Clave incorrecta");
        return u;
    }

    // Cambio de clave por ID
    public void cambioClave(String id, String claveNueva) throws DataAccessException {
        if (id == null || id.isEmpty() || claveNueva == null || claveNueva.isEmpty()) {
            throw new DataAccessException("Dato de clave incompleto");
        }
        UsuarioBase u = listUsers.obtenerPorId(id);
        if (u == null) throw new DataAccessException("Usuario no existe");
        u.setClave(claveNueva);
        listUsers.actualizar(u);
    }

    public UsuarioBase login(String id, String clave, String nombre, TipoUsuario type) throws DataAccessException {
        if (id == null || clave == null || type == null) throw new DataAccessException("Datos incompletos");
        UsuarioBase aux = listUsers.obtenerPorId(id);
        if (aux == null) throw new DataAccessException("Usuario no existe");
        if (!clave.equals(aux.getClave())) throw new DataAccessException("Clave incorrecta");
        if (aux.getTipo() != type) throw new DataAccessException("Tipo de usuario incorrecto");
        return aux;
    }

    public List<UsuarioBase> obtenerTipo(TipoUsuario tipo) {
        return listUsers.obtenerTodos().stream().filter(u -> u.getTipo() == tipo).collect(Collectors.toList());
    }
}
