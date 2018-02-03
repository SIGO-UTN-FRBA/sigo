package ar.edu.utn.frba.proyecto.sigo.domain;

import java.io.Serializable;

public abstract class SigoDomain<T extends Serializable>{

    public abstract T getId();
    public abstract void setId(T id);
}
