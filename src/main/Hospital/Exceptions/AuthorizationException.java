package Exceptions;

public class AuthorizationException extends  Exception {
    public AuthorizationException(String message) {
        super(message); //throw new AuthorizationException("Credenciales incorrectas");

    }
}
