package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayStrip;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RunwayStripService extends SigoService<RunwayStrip, RunwayDirection>{

    @Inject
    public RunwayStripService(HibernateUtil util) {
        super(RunwayStrip.class, util.getSessionFactory());
    }
}
