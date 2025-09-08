package Hospital.ManejoListas;

import  Hospital.Exceptions.DataAccessException;

import java.util.List;


 //Clase abstracta base para todas las operaciones CRUD (create, read, update, delete)


public abstract class Base<T> {

    public abstract void insertar(T objeto) throws DataAccessException;
    public abstract T obtenerPorId(String id) throws DataAccessException;
    public abstract List<T> obtenerTodos() throws DataAccessException;
    public abstract void actualizar(T objeto) throws DataAccessException;
    public abstract void eliminar(String id) throws DataAccessException;

    //agregue este para que sea mas facil la busqueda
    public abstract boolean existeId(String id) throws DataAccessException;

    //Esto es para el XML
    protected abstract void guardarEnXML() throws DataAccessException;
    protected abstract void cargarDesdeXML() throws DataAccessException;

}

