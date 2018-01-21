package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisExceptionRuleDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseService;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AnalysisExceptionRuleTranslator extends SigoTranslator<AnalysisExceptionRule, AnalysisExceptionRuleDTO> {

    private final AnalysisCaseService caseService;
    private final OlsRuleICAOAnnex14Service ruleIcaoService;
    private final RunwayDirectionService directionService;

    @Inject
    public AnalysisExceptionRuleTranslator(
            Gson objectMapper,
            AnalysisCaseService caseService,
            OlsRuleICAOAnnex14Service ruleIcaoService,
            RunwayDirectionService directionService
    ) {
        super(objectMapper, AnalysisExceptionRuleDTO.class, AnalysisExceptionRule.class);
        this.caseService = caseService;
        this.ruleIcaoService = ruleIcaoService;
        this.directionService = directionService;
    }

    @Override
    public AnalysisExceptionRuleDTO getAsDTO(AnalysisExceptionRule domain) {

        return AnalysisExceptionRuleDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .caseId(domain.getAnalysisCase().getId())
                .ruleId(domain.getRule().getId())
                .value(domain.getValue())
                .regulationId(domain.getRegulation().ordinal())
                .directionId(domain.getDirection().getId())
                .build();
    }

    @Override
    public AnalysisExceptionRule getAsDomain(AnalysisExceptionRuleDTO dto) {

        AnalysisExceptionRule.AnalysisExceptionRuleBuilder builder = AnalysisExceptionRule.builder();

        // basic properties
        builder
                .id(dto.getId())
                .name(dto.getName())
                .value(dto.getValue())
                .regulation(Regulations.values()[dto.getRegulationId()]);

        // relation: case
        AnalysisCase analysisCase = caseService.get(dto.getCaseId());
        if(!Optional.ofNullable(analysisCase).isPresent())
            throw new InvalidParameterException("caseId == " + dto.getCaseId());
        builder.analysisCase(analysisCase);

        // relation: rule
        OlsRuleICAOAnnex14 rule = ruleIcaoService.get(dto.getRuleId());
        if(!Optional.ofNullable(rule).isPresent())
            throw new InvalidParameterException("ruleId == " + dto.getRuleId());
        builder.rule(rule);

        // relation: direction
        RunwayDirection runwayDirection = directionService.get(dto.getDirectionId());
        if(!Optional.ofNullable(runwayDirection).isPresent())
            throw new InvalidParameterException("directionId == " + dto.getDirectionId());
        builder.direction(runwayDirection);


        return builder.build();
    }
}
