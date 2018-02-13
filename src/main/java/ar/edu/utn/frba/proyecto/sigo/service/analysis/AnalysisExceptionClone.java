package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionDynamicSurface;
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
                .rule(exception.getRule())
                .value(exception.getValue())
                .direction(exception.getDirection())
                .build();
    }

    @Override
    public AnalysisException visitAnalysisExceptionSurface(AnalysisExceptionSurface exception) {
        return AnalysisExceptionSurface.builder()
                .name(exception.getName())
                .heightAmls(exception.getHeightAmls())
                .geom(exception.getGeom())
                .build();
    }

    @Override
    public AnalysisException visitAnalysisExceptionDynamicSurface(AnalysisExceptionDynamicSurface exception) {
        return AnalysisExceptionDynamicSurface.builder()
                .name(exception.getName())
                .function(exception.getFunction())
                .geom(exception.getGeom())
                .build();
    }
}
