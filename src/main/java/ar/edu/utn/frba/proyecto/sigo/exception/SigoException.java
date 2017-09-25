package ar.edu.utn.frba.proyecto.sigo.exception;

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
