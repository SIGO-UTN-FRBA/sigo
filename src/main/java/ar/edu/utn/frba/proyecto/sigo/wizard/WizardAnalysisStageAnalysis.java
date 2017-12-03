package ar.edu.utn.frba.proyecto.sigo.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;

import java.util.Optional;

public class WizardAnalysisStageAnalysis extends WizardAnalysisStage{
    @Override
    protected Optional<WizardAnalysisStage> next() {
        return Optional.of(WizardAnalysisStage.of(AnalysisStages.INFORM));
    }

    @Override
    protected Optional<WizardAnalysisStage> previous() {
        return Optional.of(WizardAnalysisStage.of(AnalysisStages.EXCEPTION));
    }

    @Override
    protected void validateExit(Analysis analysis) {

    }

    @Override
    protected void validateEnter(Analysis analysis) {

    }

    @Override
    protected AnalysisStages identifier() {
        return AnalysisStages.ANALYSIS;
    }
}
