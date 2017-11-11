package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionAvailability;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionRule;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionSurface;

public interface AnalysisExceptionVisitor<T> {

    T visitAnalysisExceptionAvailability(AnalysisExceptionAvailability exception);
    T visitAnalysisExceptionRule(AnalysisExceptionRule exception);
    T visitAnalysisExceptionSurface (AnalysisExceptionSurface exception);
}
