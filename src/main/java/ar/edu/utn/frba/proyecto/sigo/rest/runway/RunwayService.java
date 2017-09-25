package ar.edu.utn.frba.proyecto.sigo.rest.runway;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.commons.service.SigoService;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;


@Singleton
public class RunwayService extends SigoService<Runway, Airport>{

    @Inject
    public RunwayService(
        HibernateUtil hibernateUtil
    ){
        this.hibernateUtil = hibernateUtil;
        this.clazz = Runway.class;
    }

    public List<Runway> getRunwaysByAirport(Airport airport){
        return hibernateUtil.doInTransaction(session -> {
            return airport.getRunways();

        });
    }

    protected void preUpdateActions(Runway newInstance, Runway oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
    }

    @Override
    protected void preCreateActions(Runway runway, Airport airport) {
        airport.addRunway(runway);
    }
}
