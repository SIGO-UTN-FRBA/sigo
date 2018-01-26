package ar.edu.utn.frba.proyecto.sigo.service.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStatuses;
import org.apache.commons.lang.NotImplementedException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public abstract class WizardAnalysisStage {

    protected abstract Optional<WizardAnalysisStage> next();

    protected abstract Optional<WizardAnalysisStage> previous();

    protected abstract void validateExit(Analysis analysis);

    protected abstract void validateEnter(Analysis analysis);

    protected abstract AnalysisStages identifier();

    protected void enter(Analysis analysis){
        analysis.setStatus(AnalysisStatuses.IN_PROGRESS);
        analysis.setStage(this.identifier());
        analysis.setEditionDate(LocalDateTime.now(ZoneOffset.UTC));
    }

    protected void finish(Analysis analysis){
        throw new NotImplementedException();
    }

    public void cancel(Analysis analysis) {
        analysis.setStatus(AnalysisStatuses.CANCELLED);
        analysis.setEditionDate(LocalDateTime.now(ZoneOffset.UTC));
    }
}
