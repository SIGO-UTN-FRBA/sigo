package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisAdverseEffectAspect;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisAdverseEffectMitigation;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisAdverseEffectMitigationService extends SigoService<AnalysisAdverseEffectMitigation, AnalysisAdverseEffectAspect> {

    @Inject
    public AnalysisAdverseEffectMitigationService(SessionFactory sessionFactory) {
        super(AnalysisAdverseEffectMitigation.class, sessionFactory);
    }
}
