package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectBuilding;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlacedObjectBuildingService extends SigoService<PlacedObjectBuilding, Region> {

    @Inject
    public PlacedObjectBuildingService(SessionFactory sessionFactory) {
        super(PlacedObjectBuilding.class, sessionFactory);
    }
}
