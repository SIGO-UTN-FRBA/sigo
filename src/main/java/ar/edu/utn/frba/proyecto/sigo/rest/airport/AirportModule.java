package ar.edu.utn.frba.proyecto.sigo.rest.airport;

import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class AirportModule extends AbstractModule {

    @Override
    protected void configure() {

        Multibinder<Router> routerBinder = Multibinder.newSetBinder(binder(), Router.class);

        bind(AirportRouter.class);
        bind(AirportService.class);

        routerBinder.addBinding().to(AirportRouter.class);
    }
}
