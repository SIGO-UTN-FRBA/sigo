package ar.edu.utn.frba.proyecto.sigo.exception;

public class UnauthorizedRequestException extends SigoException {

    public UnauthorizedRequestException(String message) {
        super(message);
    }

    public UnauthorizedRequestException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
