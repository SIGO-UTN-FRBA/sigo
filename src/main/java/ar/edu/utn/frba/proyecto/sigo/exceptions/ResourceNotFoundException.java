package ar.edu.utn.frba.proyecto.sigo.exceptions;

public class ResourceNotFoundException extends SigoException{

    public ResourceNotFoundException(String resource) {
        super("Cannot found resource " + resource);
    }
}
