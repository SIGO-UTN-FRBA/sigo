package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionRule;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisExceptionRuleService extends SigoService<AnalysisExceptionRule, AnalysisCase>{

    @Inject
    public AnalysisExceptionRuleService(HibernateUtil util) {
        super(AnalysisExceptionRule.class, util.getSessionFactory());
    }
}
