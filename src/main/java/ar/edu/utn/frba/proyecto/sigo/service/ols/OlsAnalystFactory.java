package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;

public interface OlsAnalystFactory {

    OlsAnalystICAOAnnex14 createAnalystICAOAnnex14(AnalysisCase analysisCase);
}
