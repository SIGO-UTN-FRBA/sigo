package ar.edu.utn.frba.proyecto.sigo.rest;

import ar.edu.utn.frba.proyecto.sigo.rest.airport.AirportRouter;
import ar.edu.utn.frba.proyecto.sigo.rest.airport.AirportService;
import ar.edu.utn.frba.proyecto.sigo.rest.airport.AirportTranslator;
import ar.edu.utn.frba.proyecto.sigo.rest.runway.RunwayRouter;
import ar.edu.utn.frba.proyecto.sigo.rest.runway.RunwayService;
import ar.edu.utn.frba.proyecto.sigo.rest.runway.RunwayTranslator;
import ar.edu.utn.frba.proyecto.sigo.rest.runway.direction.RunwayDirectionRouter;
import ar.edu.utn.frba.proyecto.sigo.rest.runway.direction.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.rest.runway.direction.RunwayDirectionTranslator;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class RestModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(AirportRouter.class);
        bind(AirportService.class);
        bind(AirportTranslator.class);
        bind(RunwayRouter.class);
        bind(RunwayService.class);
        bind(RunwayTranslator.class);
        bind(RunwayDirectionRouter.class);
        bind(RunwayDirectionService.class);
        bind(RunwayDirectionTranslator.class);


        Multibinder<Router> routerBinder = Multibinder.newSetBinder(binder(), Router.class);

        routerBinder.addBinding().to(RunwayRouter.class);
        routerBinder.addBinding().to(AirportRouter.class);
        routerBinder.addBinding().to(RunwayDirectionRouter.class);
    }
}
