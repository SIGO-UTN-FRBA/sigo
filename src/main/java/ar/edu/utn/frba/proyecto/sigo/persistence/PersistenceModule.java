package ar.edu.utn.frba.proyecto.sigo.persistence;

import com.google.inject.AbstractModule;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(HibernateUtil.class);
    }
}
