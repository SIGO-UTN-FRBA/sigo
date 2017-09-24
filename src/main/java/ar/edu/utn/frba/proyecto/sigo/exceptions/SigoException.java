package ar.edu.utn.frba.proyecto.sigo.exceptions;

public class SigoException extends RuntimeException{

    public SigoException(String message) {
        super(message);
    }

    public SigoException(Throwable cause) {
        super(cause);
    }

    public SigoException(String message, Throwable cause) {
        super(message, cause);
    }

}
