// src/main/java/Logic/Listas/Base.java
package Logic.Listas;

import Data.XMLPersister;
import Logic.Exceptions.DataAccessException;

import java.util.List;

public abstract class Base<T> {

    protected static XMLPersister xmlPersister = XMLPersister.instance();
    protected static XMLPersister.Data data;

    public Base() {
        if (data == null) {
            try {
                data = xmlPersister.load();
            } catch (DataAccessException e) {
                data = new XMLPersister.Data();
            }
        }
    }

    public abstract void insertar(T objeto) throws DataAccessException;
    public abstract T obtenerPorId(String id) throws DataAccessException;
    public abstract List<T> obtenerTodos() throws DataAccessException;
    public abstract void actualizar(T objeto) throws DataAccessException;
    public abstract void eliminar(String id) throws DataAccessException;
    public abstract boolean existeId(String id) throws DataAccessException;

    protected void guardarEnXML() throws DataAccessException { xmlPersister.store(data); }
    protected void cargarDesdeXML() throws DataAccessException { data = xmlPersister.load(); }
}
