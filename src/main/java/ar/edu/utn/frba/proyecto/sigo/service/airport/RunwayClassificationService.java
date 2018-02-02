package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayClassification;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class RunwayClassificationService extends SigoService<RunwayClassification, RunwayClassification>{

    @Inject
    public RunwayClassificationService(SessionFactory sessionFactory) {
        super(RunwayClassification.class, sessionFactory);
    }
}
