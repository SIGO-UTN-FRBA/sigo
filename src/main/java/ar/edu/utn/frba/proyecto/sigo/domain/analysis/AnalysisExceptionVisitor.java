package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

public interface AnalysisExceptionVisitor<T> {

    T visitAnalysisExceptionRule(AnalysisExceptionRule exception);
    T visitAnalysisExceptionSurface (AnalysisExceptionSurface exception);
}
