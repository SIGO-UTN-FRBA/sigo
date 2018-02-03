package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisResultReason;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class AnalysisResultReasonService extends SigoService<AnalysisResultReason, AnalysisResultReason>{

    @Inject
    public AnalysisResultReasonService(SessionFactory sessionFactory) {
        super(AnalysisResultReason.class, sessionFactory);
    }
}
