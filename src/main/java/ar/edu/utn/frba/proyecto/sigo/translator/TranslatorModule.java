package ar.edu.utn.frba.proyecto.sigo.translator;

import ar.edu.utn.frba.proyecto.sigo.translator.airport.*;
import ar.edu.utn.frba.proyecto.sigo.translator.analysis.*;
import ar.edu.utn.frba.proyecto.sigo.translator.location.PoliticalLocationTranslator;
import com.google.inject.AbstractModule;

public class TranslatorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RunwayTranslator.class);
        bind(AnalysisObjectTranslator.class);
        bind(AnalysisExceptionTranslator.class);
        bind(AnalysisTranslator.class);
        bind(AnalysisCaseTranslator.class);
        bind(PoliticalLocationTranslator.class);
        bind(SimpleFeatureTranslator.class);
        bind(RunwayTakeoffSectionTranslator.class);
        bind(RunwayApproachSectionTranslator.class);
        bind(RunwayDirectionTranslator.class);
        bind(RunwayClassificationTranslator.class);
        bind(AnalysisAdverseEffectMitigationTranslator.class);
        bind(AnalysisAdverseEffectAspectTranslator.class);
    }
}
