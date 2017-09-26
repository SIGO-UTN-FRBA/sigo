package ar.edu.utn.frba.proyecto.sigo.rest.runway.direction;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.commons.service.SigoService;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwayDirection;

import javax.inject.Inject;

public class RunwayDirectionService extends SigoService<RunwayDirection, Runway>{

    @Inject
    public RunwayDirectionService(
            HibernateUtil hibernateUtil
    ){
        this.hibernateUtil = hibernateUtil;
        this.clazz = RunwayDirection.class;
    }
}
