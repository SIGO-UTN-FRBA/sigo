package ar.edu.utn.frba.proyecto.sigo.service.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class WizardAnalysisStageObject extends WizardAnalysisStage {

    Provider<WizardAnalysisStageException> next;

    @Inject
    public WizardAnalysisStageObject(
            Provider<WizardAnalysisStageException> next
    ){
        this.next = next;
    }

    @Override
    protected Optional<WizardAnalysisStage> next() {
        return Optional.of(next.get());
    }

    @Override
    protected Optional<WizardAnalysisStage> previous() {
        return Optional.empty();
    }

    @Override
    protected void validateExit(Analysis analysis) {
        if(!analysis.getAnalysisCase().getObjects().stream().anyMatch(o -> o.getIncluded()))
            throw new RuntimeException("There is no object included into analysis case");
    }

    @Override
    protected void validateEnter(Analysis analysis) {
        //nothing to do
    }

    @Override
    protected AnalysisStages identifier() {
        return AnalysisStages.OBJECT;
    }
}
