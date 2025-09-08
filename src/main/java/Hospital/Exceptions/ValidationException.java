package Hospital.Exceptions;

public class ValidationException extends Exception { //Datos invalidos
    public ValidationException(String message) {
        super(message);
    }
}
