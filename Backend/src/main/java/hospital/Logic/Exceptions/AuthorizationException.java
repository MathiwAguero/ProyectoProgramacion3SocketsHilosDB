package hospital.Logic.Exceptions;

public class AuthorizationException extends  Exception { //Errores de permisos
    public AuthorizationException(String message) {
        super(message); //throw new AuthorizationException("Credenciales incorrectas");

    }
}
