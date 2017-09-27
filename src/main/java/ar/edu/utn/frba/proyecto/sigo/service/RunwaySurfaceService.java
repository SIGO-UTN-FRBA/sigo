package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.domain.RunwaySurface;

import javax.inject.Inject;

public class RunwaySurfaceService extends SigoService<RunwaySurface, RunwaySurface>{

    @Inject
    public RunwaySurfaceService(
            HibernateUtil hibernateUtil) {
        super(RunwaySurface.class, hibernateUtil.getSessionFactory());
    }
}
