package ar.edu.utn.frba.proyecto.sigo.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStatuses;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public abstract class WizardAnalysisStage {

    public static WizardAnalysisStage of(AnalysisStages stage){
        switch (stage) {
            case OBJECT:
                return new WizardAnalysisStageObject();
            case EXCEPTION:
                return new WizardAnalysisStageException();
            case ANALYSIS:
                return new WizardAnalysisStageAnalysis();
            case INFORM:
                return new WizardAnalysisStageInform();
        }

        throw new NotImplementedException();
    }

    protected abstract Optional<WizardAnalysisStage> next();

    protected abstract Optional<WizardAnalysisStage> previous();

    protected abstract void validateExit(Analysis analysis);

    protected abstract void validateEnter(Analysis analysis);

    protected abstract AnalysisStages identifier();

    protected void enter(Analysis analysis){
        analysis.setStage(this.identifier());
        analysis.setEditionDate(LocalDateTime.now(ZoneOffset.UTC));
    }

    protected void finish(Analysis analysis){
        throw new RuntimeException(); //TODO especializar exception
    }

    public void cancel(Analysis analysis) {
        analysis.setStatus(AnalysisStatuses.CANCELLED);
        analysis.setEditionDate(LocalDateTime.now(ZoneOffset.UTC));
    }
}
