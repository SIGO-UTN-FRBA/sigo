package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionRule;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionVisitor;

import javax.inject.Singleton;

@Singleton
public class AnalysisExceptionClone implements AnalysisExceptionVisitor<AnalysisException>{

    @Override
    public AnalysisException visitAnalysisExceptionRule(AnalysisExceptionRule exception) {
        return AnalysisExceptionRule.builder()
                .name(exception.getName())
                .type(exception.getType())
                .olsRuleId(exception.getOlsRuleId())
                .property(exception.getProperty())
                .value(exception.getValue())
                .build();
    }

    @Override
    public AnalysisException visitAnalysisExceptionSurface(AnalysisExceptionSurface exception) {
        return AnalysisExceptionSurface.builder()
                .name(exception.getName())
                .type(exception.getType())
                .properties(exception.getProperties())
                .build();
    }
}
