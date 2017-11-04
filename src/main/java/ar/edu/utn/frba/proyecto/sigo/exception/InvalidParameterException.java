package ar.edu.utn.frba.proyecto.sigo.exception;

public class InvalidParameterException extends SigoException {

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
