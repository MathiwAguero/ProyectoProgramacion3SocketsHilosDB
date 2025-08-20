package DataAccessObject;

import Exceptions.DataAccessException;
import java.util.List;

/*
 * Clase abstracta base para todas las operaciones CRUD
 * Cada DAO específico extenderá esta clase
 */

public abstract class BaseDAO<T> {

    public abstract void insertar(T objeto) throws DataAccessException;
    public abstract T obtenerPorId(String id) throws DataAccessException;
    public abstract List<T> obtenerTodos() throws DataAccessException;
    public abstract void actualizar(T objeto) throws DataAccessException;
    public abstract void eliminar(String id) throws DataAccessException;
    public abstract boolean existeId(String id) throws DataAccessException;
}

