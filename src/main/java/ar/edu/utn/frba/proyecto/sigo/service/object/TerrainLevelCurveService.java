package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.TerrainLevelCurve;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TerrainLevelCurveService extends SigoService<TerrainLevelCurve,TerrainLevelCurve>{

    @Inject
    public TerrainLevelCurveService(SessionFactory sessionFactory) {
        super(TerrainLevelCurve.class, sessionFactory);
    }
}
