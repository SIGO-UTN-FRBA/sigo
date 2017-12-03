package ar.edu.utn.frba.proyecto.sigo.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.inject.Singleton;

@Singleton
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WizardAnalysis {

    public void next(Analysis analysis){
        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        WizardAnalysisStage nextStage = currentStage.next().orElseThrow(()-> new RuntimeException("Invalid wizard stage transition"));

        currentStage.validateExit(analysis);

        nextStage.validateEnter(analysis);

        nextStage.enter(analysis);
    }

    public void previous(Analysis analysis){
        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        WizardAnalysisStage previousStage = currentStage.previous().orElseThrow(()-> new RuntimeException("Invalid wizard stage transition"));

        previousStage.enter(analysis);
    }

    public void finish(Analysis analysis){
        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        currentStage.finish(analysis);
    }

    public void cancel(Analysis analysis){
        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        currentStage.cancel(analysis);
    }

    private WizardAnalysisStage getCurrentStage(Analysis analysis) {
        return WizardAnalysisStage.of(analysis.getStage());
    }
}
