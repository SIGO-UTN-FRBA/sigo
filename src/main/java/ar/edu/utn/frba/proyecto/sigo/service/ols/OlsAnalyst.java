package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import lombok.Data;

@Data
public abstract class OlsAnalyst {

    protected AnalysisCase analysisCase;

    OlsAnalyst(AnalysisCase analysisCase) {
        this.analysisCase = analysisCase;
    }

    public void analyze(){

        initializeSurfaces();

        defineObstacles();

        applyExceptions();

    }

    protected abstract void applyExceptions();

    protected abstract void defineObstacles();

    protected abstract void initializeSurfaces();
}
