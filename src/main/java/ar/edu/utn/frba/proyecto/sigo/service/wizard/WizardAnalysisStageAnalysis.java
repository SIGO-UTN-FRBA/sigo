package ar.edu.utn.frba.proyecto.sigo.service.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;
import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalyst;
import ar.edu.utn.frba.proyecto.sigo.service.ols.OlsAnalystFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class WizardAnalysisStageAnalysis extends WizardAnalysisStage{

    private OlsAnalystFactory analystFactory;
    private Provider<WizardAnalysisStageInform> next;
    private Provider<WizardAnalysisStageException> previous;

    @Inject
    public WizardAnalysisStageAnalysis(
            OlsAnalystFactory analystFactory,
            Provider<WizardAnalysisStageInform> next,
            Provider<WizardAnalysisStageException> previous
    ) {
        this.analystFactory = analystFactory;
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

    }

    @Override
    protected void validateEnter(Analysis analysis) {

    }

    @Override
    protected AnalysisStages identifier() {
        return AnalysisStages.ANALYSIS;
    }

    @Override
    protected void enter(Analysis analysis) {
        super.enter(analysis);

        analystFactory.createAnalystICAOAnnex14(analysis.getAnalysisCase())
                .analyze();
    }
}
