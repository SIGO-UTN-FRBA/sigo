package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.service.AirportService;
import ar.edu.utn.frba.proyecto.sigo.service.AirportTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayService;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.service.RunwayDirectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class RouterModule extends AbstractModule {

    @Override
    protected void configure() {

        Multibinder<Router> routerBinder = Multibinder.newSetBinder(binder(), Router.class);

        bind(AirportRouter.class);
        bind(AirportService.class);
        bind(AirportTranslator.class);
        bind(RunwayRouter.class);
        bind(RunwayService.class);
        bind(RunwayTranslator.class);
        bind(RunwayDirectionRouter.class);
        bind(RunwayDirectionService.class);
        bind(RunwayDirectionTranslator.class);

        routerBinder.addBinding().to(AirportRouter.class);
        routerBinder.addBinding().to(RunwayRouter.class);
        routerBinder.addBinding().to(RunwayDirectionRouter.class);
    }
}
