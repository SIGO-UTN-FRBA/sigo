package ar.edu.utn.frba.proyecto.sigo.service.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStatuses;
import ar.edu.utn.frba.proyecto.sigo.exception.BusinessConstrainException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

        if(analysis.getAnalysisCase().getObstacles().isEmpty())
            return;

        boolean pending = analysis.getAnalysisCase().getObstacles()
                .stream()
                .anyMatch(o -> !o.getExcepting() && !Optional.ofNullable(o.getResult()).isPresent());

        if(pending)
            throw new BusinessConstrainException("There are some obstacles have not assigned their result yet.");
    }

    @Override
    protected AnalysisStages identifier() {
        return AnalysisStages.INFORM;
    }

    @Override
    protected void finish(Analysis analysis) {
        analysis.setStatus(AnalysisStatuses.FINISHED);
        analysis.setEditionDate(LocalDateTime.now(ZoneOffset.UTC));
    }
}
