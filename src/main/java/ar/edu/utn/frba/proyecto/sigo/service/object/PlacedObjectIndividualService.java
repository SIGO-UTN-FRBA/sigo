package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlacedObjectIndividualService extends SigoService<PlacedObjectIndividual, Region> {

    @Inject
    public PlacedObjectIndividualService(HibernateUtil util) {
        super(PlacedObjectIndividual.class, util.getSessionFactory());
    }
}
