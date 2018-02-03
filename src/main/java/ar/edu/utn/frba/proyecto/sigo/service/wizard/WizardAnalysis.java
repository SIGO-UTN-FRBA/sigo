package ar.edu.utn.frba.proyecto.sigo.service.wizard;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStages;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisStatuses;
import ar.edu.utn.frba.proyecto.sigo.exception.BusinessConstrainException;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import ar.edu.utn.frba.proyecto.sigo.security.SigoRoles;
import ar.edu.utn.frba.proyecto.sigo.security.UserSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
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

    public void start(Analysis analysis, UserSession currentUserSession){

        validateCrossUserEdition(analysis, currentUserSession);

        validateStart(analysis);

        analysis.setStatus(AnalysisStatuses.IN_PROGRESS);

        analysis.setStage(AnalysisStages.OBJECT);
    }

    private void validateStart(Analysis analysis) {
        if(!analysis.getStatus().equals(AnalysisStatuses.INITIALIZED)){
            throw new BusinessConstrainException("Fail to start wizard because the analysis is not in a initial state");
        }
    }

    public void goNext(Analysis analysis, UserSession currentUserSession){

        validateCrossUserEdition(analysis, currentUserSession);

        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        WizardAnalysisStage nextStage = currentStage.next().orElseThrow(()-> new RuntimeException("Invalid wizard stage transition"));

        currentStage.validateExit(analysis);

        nextStage.validateEnter(analysis);

        nextStage.enter(analysis);
    }

    public void goPrevious(Analysis analysis, UserSession currentUserSession){

        validateCrossUserEdition(analysis, currentUserSession);

        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        WizardAnalysisStage previousStage = currentStage.previous().orElseThrow(()-> new RuntimeException("Invalid wizard stage transition"));

        currentStage.rollback(analysis);

        previousStage.enter(analysis);
    }

    public void finish(Analysis analysis, UserSession currentUserSession){

        validateCrossUserEdition(analysis, currentUserSession);

        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        currentStage.finish(analysis);
    }

    public void cancel(Analysis analysis, UserSession currentUserSession){

        validateCrossUserEdition(analysis, currentUserSession);

        WizardAnalysisStage currentStage = getCurrentStage(analysis);

        currentStage.cancel(analysis);
    }

    public void validateCrossUserEdition(Analysis analysis, UserSession currentUserSession) {
        if(currentUserSession.getRole().equals(SigoRoles.WORKER) && !analysis.getUser().getId().equals(currentUserSession.getUser().getId())){
            throw new BusinessConstrainException("Cannot edit an analysis case that it is performed by other user.");
        }
    }

    private WizardAnalysisStage getCurrentStage(Analysis analysis) {

        return stages
                .stream()
                .filter(s -> analysis.getStage().equals(s.identifier()))
                .findFirst()
                .orElseThrow(()-> new SigoException("Invalid wizard stage"));
    }
}
