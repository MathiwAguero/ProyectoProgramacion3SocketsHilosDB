// src/main/java/Logic/Listas/Base.java
package hospital.Logic.Listas;

import hospital.Data.*;
import hospital.Entities.*;
import hospital.Logic.Exceptions.DataAccessException;

import java.util.List;

public abstract class Base<T> {

    protected static XMLPersister xmlPersister = XMLPersister.instance();
    protected static XMLPersister.Data data;

    public Base() {
        try {
            if (data == null) {
                data = xmlPersister.load();
            }
        } catch (Exception e) {
            System.err.println("Error inicializando Base: " + e.getMessage());

            data = new XMLPersister.Data();
        }
    }

    public abstract void insertar(T objeto) throws DataAccessException, hospital.Logic.Exceptions.DataAccessException;
    public abstract T obtenerPorId(String id) throws DataAccessException;
    public abstract List<T> obtenerTodos() throws DataAccessException;
    public abstract void actualizar(T objeto) throws DataAccessException, hospital.Logic.Exceptions.DataAccessException;
    public abstract void eliminar(String id) throws DataAccessException, hospital.Logic.Exceptions.DataAccessException;
    public abstract boolean existeId(String id) throws DataAccessException;

    protected void guardarEnXML() throws DataAccessException { xmlPersister.store(data); }
    protected void cargarDesdeXML() throws DataAccessException { data = xmlPersister.load(); }
}
