package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.service.ols.icao.OlsAnalystICAOAnnex14;

public interface OlsAnalystFactory {

    OlsAnalystICAOAnnex14 createAnalystICAOAnnex14(AnalysisCase analysisCase);
}
