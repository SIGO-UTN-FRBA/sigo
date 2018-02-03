package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlacedObjectIndividualService extends SigoService<PlacedObjectIndividual, Region> {

    @Inject
    public PlacedObjectIndividualService(SessionFactory sessionFactory) {
        super(PlacedObjectIndividual.class, sessionFactory);
    }
}
