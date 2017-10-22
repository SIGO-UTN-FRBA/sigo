package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayClassification;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;

public class RunwayClassificationService extends SigoService<RunwayClassification, RunwayClassification>{

    @Inject
    public RunwayClassificationService(HibernateUtil hibernateUtil) {
        super(RunwayClassification.class, hibernateUtil.getSessionFactory());
    }
}
