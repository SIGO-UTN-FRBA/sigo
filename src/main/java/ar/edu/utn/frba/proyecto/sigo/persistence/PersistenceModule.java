package ar.edu.utn.frba.proyecto.sigo.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.hibernate.SessionFactory;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HibernateUtil.class);
    }

    @Provides
    SessionFactory provideSessionFactoryHibernate(HibernateUtil util){
        return util.getSessionFactory();
    }
}
