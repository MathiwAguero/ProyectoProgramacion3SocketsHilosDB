package Hospital.Exceptions;

public class DataAccessException extends Exception { //Errores al cargar datos/guardar
    public DataAccessException(String message) {
        super(message);
    }
}
