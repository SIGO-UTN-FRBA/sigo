package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayStrip;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RunwayStripService extends SigoService<RunwayStrip, Runway>{

    @Inject
    public RunwayStripService(HibernateUtil util) {
        super(RunwayStrip.class, util.getSessionFactory());
    }
}
