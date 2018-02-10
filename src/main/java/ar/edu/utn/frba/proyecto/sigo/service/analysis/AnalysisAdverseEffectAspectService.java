package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisAdverseEffectAspect;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisAdverseEffectAspectService extends SigoService<AnalysisAdverseEffectAspect, AnalysisAdverseEffectAspect>{

    @Inject
    public AnalysisAdverseEffectAspectService(SessionFactory sessionFactory) {
        super(AnalysisAdverseEffectAspect.class, sessionFactory);
    }
}
