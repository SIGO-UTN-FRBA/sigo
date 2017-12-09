package ar.edu.utn.frba.proyecto.sigo.service.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class WizardAnalysisStageException extends WizardAnalysisStage {

    private Provider<WizardAnalysisStageAnalysis> next;
    private Provider<WizardAnalysisStageObject> previous;

    @Inject
    public WizardAnalysisStageException(
            Provider<WizardAnalysisStageAnalysis> next,
            Provider<WizardAnalysisStageObject> previous
    ){
        this.next = next;
        this.previous = previous;
    }

    @Override
    protected Optional<WizardAnalysisStage> next() {
        return Optional.of(next.get());
    }

    @Override
    protected Optional<WizardAnalysisStage> previous() {
        return Optional.of(previous.get());
    }

    @Override
    protected void validateExit(Analysis analysis) {
        //nothing to do
    }

    @Override
    protected void validateEnter(Analysis analysis) {
        //nothing to do
    }

    @Override
    protected AnalysisStages identifier() {
        return AnalysisStages.EXCEPTION;
    }
}
