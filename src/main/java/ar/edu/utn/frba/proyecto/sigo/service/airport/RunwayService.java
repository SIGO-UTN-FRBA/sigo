package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class RunwayService extends SigoService<Runway, Airport> {

    @Override
    protected void preCreateActions(Runway instance) {
        super.preCreateActions(instance);
    }

    @Inject
    public RunwayService(HibernateUtil hibernateUtil){
        super(Runway.class, hibernateUtil.getSessionFactory());
    }

    protected void preUpdateActions(Runway newInstance, Runway oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
    }

    @Override
    protected void preCreateActions(Runway runway, Airport airport) {
        airport.addRunway(runway);
    }

    public List<RunwayDirection> getDirections(Runway runway) {
        return runway.getDirections();
    }
}
