package ar.edu.utn.frba.proyecto.sigo.exception;

public class MissingParameterException extends SigoException {

    public MissingParameterException(String parameter) {
        super(String.format("Missing parameter '%s'", parameter));
    }
}
