package ar.edu.utn.frba.proyecto.sigo.router;

import ar.edu.utn.frba.proyecto.sigo.router.airport.*;
import ar.edu.utn.frba.proyecto.sigo.router.analysis.*;
import ar.edu.utn.frba.proyecto.sigo.router.location.LocationRouter;
import ar.edu.utn.frba.proyecto.sigo.router.object.CatalogObjectRouter;
import ar.edu.utn.frba.proyecto.sigo.router.object.ElevatedObjectRouter;
import ar.edu.utn.frba.proyecto.sigo.router.object.ObjectOwnerRouter;
import ar.edu.utn.frba.proyecto.sigo.router.regulation.RegulationFAARouter;
import ar.edu.utn.frba.proyecto.sigo.router.regulation.RegulationICAOAnnex14Router;
import ar.edu.utn.frba.proyecto.sigo.router.regulation.RegulationRouter;
import ar.edu.utn.frba.proyecto.sigo.router.wizard.WizardAnalysisRouter;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class RouterModule extends AbstractModule {

    @Override
    protected void configure() {

        Multibinder<Router> routerBinder = Multibinder.newSetBinder(binder(), Router.class);

        routerBinder.addBinding().to(AirportRouter.class);
        routerBinder.addBinding().to(RunwayRouter.class);
        routerBinder.addBinding().to(RunwayDirectionRouter.class);
        routerBinder.addBinding().to(CatalogAirportRouter.class);
        routerBinder.addBinding().to(RegionRouter.class);
        routerBinder.addBinding().to(CatalogObjectRouter.class);
        routerBinder.addBinding().to(ElevatedObjectRouter.class);
        routerBinder.addBinding().to(ObjectOwnerRouter.class);
        routerBinder.addBinding().to(LocationRouter.class);
        routerBinder.addBinding().to(RegulationRouter.class);
        routerBinder.addBinding().to(RegulationICAOAnnex14Router.class);
        routerBinder.addBinding().to(RegulationFAARouter.class);
        routerBinder.addBinding().to(AnalysisRouter.class);
        routerBinder.addBinding().to(AnalysisCaseRouter.class);
        routerBinder.addBinding().to(AnalysisExceptionRouter.class);
        routerBinder.addBinding().to(AnalysisObjectRouter.class);
        routerBinder.addBinding().to(WizardAnalysisRouter.class);
        routerBinder.addBinding().to(AnalysisSurfaceRouter.class);
        routerBinder.addBinding().to(AnalysisObstacleRouter.class);
        routerBinder.addBinding().to(CatalogAnalysisRouter.class);
    }
}
