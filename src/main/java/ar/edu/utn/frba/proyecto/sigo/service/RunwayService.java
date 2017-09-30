package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwayDirection;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class RunwayService extends SigoService<Runway, Airport>{

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
