package ar.edu.utn.frba.proyecto.sigo.commons.persistence;

import com.google.inject.AbstractModule;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        //bind(PostgresqlDataSource.class);
        bind(HibernateUtil.class);
    }
}
