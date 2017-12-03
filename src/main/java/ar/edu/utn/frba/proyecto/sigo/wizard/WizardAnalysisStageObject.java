package ar.edu.utn.frba.proyecto.sigo.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WizardAnalysisStageObject extends WizardAnalysisStage {

    @Override
    protected Optional<WizardAnalysisStage> next() {
        return Optional.of(WizardAnalysisStage.of(AnalysisStages.EXCEPTION));
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
