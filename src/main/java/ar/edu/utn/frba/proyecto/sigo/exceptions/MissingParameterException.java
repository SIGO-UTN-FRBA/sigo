package ar.edu.utn.frba.proyecto.sigo.exceptions;

public class MissingParameterException extends SigoException {

    public MissingParameterException(String parameter) {
        super(String.format("Missing parameter '%s'", parameter));
    }
}
