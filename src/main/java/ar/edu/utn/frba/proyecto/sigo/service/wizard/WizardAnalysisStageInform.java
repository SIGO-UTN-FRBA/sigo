package ar.edu.utn.frba.proyecto.sigo.service.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class WizardAnalysisStageInform extends WizardAnalysisStage {

    private Provider<WizardAnalysisStageAnalysis> previous;

    @Inject
    public WizardAnalysisStageInform(
            Provider<WizardAnalysisStageAnalysis> previous) {
        this.previous = previous;
    }

    @Override
    protected Optional<WizardAnalysisStage> next() {
        return Optional.empty();
    }

    @Override
    protected Optional<WizardAnalysisStage> previous() {
        return Optional.of(previous.get());
    }

    @Override
    protected void validateExit(Analysis analysis) {

    }

    @Override
    protected void validateEnter(Analysis analysis) {

    }

    @Override
    protected AnalysisStages identifier() {
        return AnalysisStages.INFORM;
    }
}
