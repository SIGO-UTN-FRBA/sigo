package ar.edu.utn.frba.proyecto.sigo.rest.runway;

import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class RunwayModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Router> routerBinder = Multibinder.newSetBinder(binder(), Router.class);

        bind(RunwayRouter.class);
        bind(RunwayService.class);

        routerBinder.addBinding().to(RunwayRouter.class);
    }
}
