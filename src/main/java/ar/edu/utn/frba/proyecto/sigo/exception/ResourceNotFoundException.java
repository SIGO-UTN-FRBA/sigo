package ar.edu.utn.frba.proyecto.sigo.exception;

public class ResourceNotFoundException extends SigoException{

    public ResourceNotFoundException(String resource) {
        super("Cannot found resource " + resource);
    }
}
