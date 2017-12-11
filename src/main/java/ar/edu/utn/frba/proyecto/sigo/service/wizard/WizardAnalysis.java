package ar.edu.utn.frba.proyecto.sigo.service.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WizardAnalysis {

    private Set<WizardAnalysisStage> stages;

    @Inject
    public WizardAnalysis(
        Set<WizardAnalysisStage> stages
    ) {
        this.stages = stages;
    }

    public void goNext(Analysis analysis){
        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        WizardAnalysisStage nextStage = currentStage.next().orElseThrow(()-> new RuntimeException("Invalid wizard stage transition"));

        currentStage.validateExit(analysis);

        nextStage.validateEnter(analysis);

        nextStage.enter(analysis);
    }

    public void goPrevious(Analysis analysis){
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

        return stages
                .stream()
                .filter(s -> analysis.getStage().equals(s.identifier()))
                .findFirst()
                .orElseThrow(()-> new SigoException("Invalid wizard stage"));
    }
}
