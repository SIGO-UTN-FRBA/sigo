package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionDynamicSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionRule;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionVisitor;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptions;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisExceptionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AnalysisExceptionTranslator extends Translator<AnalysisException, AnalysisExceptionDTO>{

    private AnalysisCaseService caseService;
    private OlsRuleICAOAnnex14Service ruleIcaoService;
    private RunwayDirectionService directionService;

    @Inject
    public AnalysisExceptionTranslator(
        Gson objectMapper,
        AnalysisCaseService caseService,
        OlsRuleICAOAnnex14Service ruleIcaoService,
        RunwayDirectionService directionService
    ){
        this.caseService = caseService;
        this.ruleIcaoService = ruleIcaoService;
        this.directionService = directionService;
        this.objectMapper = objectMapper;
        this.dtoClass = AnalysisExceptionDTO.class;
        this.domainClass = AnalysisException.class;
    }

    public AnalysisExceptionDTO getAsAbstractDTO(AnalysisException domain) {
        return AnalysisExceptionDTO.builder()
                .id(domain.getId())
                .caseId(domain.getAnalysisCase().getId())
                .name(domain.getName())
                .typeId(domain.getType().ordinal())
                .build();
    }

    @Override
    public AnalysisExceptionDTO getAsDTO(AnalysisException domain) {
        return domain.accept(new ToDTO());
    }

    @Override
    public AnalysisException getAsDomain(AnalysisExceptionDTO dto) {

        switch (AnalysisExceptions.values()[dto.getTypeId()]){
            case SURFACE:
                return this.getAsSurfaceDomain(dto);
            case RULE:
                return this.getAsRuleDomain(dto);
            case DYNAMIC_SURFACE:
                return this.getAsDynamicSurfaceDomain(dto);
        }

        throw new InvalidParameterException("typeId does not exist");
    }

    private class ToDTO implements AnalysisExceptionVisitor<AnalysisExceptionDTO>{

        @Override
        public AnalysisExceptionDTO visitAnalysisExceptionRule(AnalysisExceptionRule exception) {
            return AnalysisExceptionDTO.builder()
                    .id(exception.getId())
                    .name(exception.getName())
                    .caseId(exception.getAnalysisCase().getId())
                    .ruleId(exception.getRule().getId())
                    .value(exception.getValue())
                    .regulationId(exception.getRegulation().ordinal())
                    .directionId(exception.getDirection().getId())
                    .build();
        }

        @Override
        public AnalysisExceptionDTO visitAnalysisExceptionSurface(AnalysisExceptionSurface exception) {
            return AnalysisExceptionDTO.builder()
                    .id(exception.getId())
                    .name(exception.getName())
                    .caseId(exception.getAnalysisCase().getId())
                    .heightAgl(exception.getHeightAgl())
                    .build();
        }

        @Override
        public AnalysisExceptionDTO visitAnalysisExceptionDynamicSurface(AnalysisExceptionDynamicSurface exception) {
            return AnalysisExceptionDTO.builder()
                    .id(exception.getId())
                    .name(exception.getName())
                    .caseId(exception.getAnalysisCase().getId())
                    .function(exception.getFunction())
                    .build();
        }
    }

    private AnalysisExceptionSurface getAsSurfaceDomain(AnalysisExceptionDTO dto) {

        AnalysisExceptionSurface.AnalysisExceptionSurfaceBuilder builder = AnalysisExceptionSurface.builder();

        // basic properties
        builder
            .id(dto.getId())
            .name(dto.getName())
            .type(AnalysisExceptions.SURFACE)
            .heightAgl(dto.getHeightAgl());

        // relation: case
        AnalysisCase analysisCase = caseService.get(dto.getCaseId());
        if(!Optional.ofNullable(analysisCase).isPresent())
            throw new InvalidParameterException("caseId == " + dto.getCaseId());
        builder.analysisCase(analysisCase);

        return builder.build();
    }

    private AnalysisExceptionDynamicSurface getAsDynamicSurfaceDomain(AnalysisExceptionDTO dto) {

        AnalysisExceptionDynamicSurface.AnalysisExceptionDynamicSurfaceBuilder builder = AnalysisExceptionDynamicSurface.builder();

        // basic properties
        builder
                .id(dto.getId())
                .name(dto.getName())
                .type(AnalysisExceptions.DYNAMIC_SURFACE)
                .function(dto.getFunction());

        // relation: case
        AnalysisCase analysisCase = caseService.get(dto.getCaseId());
        if(!Optional.ofNullable(analysisCase).isPresent())
            throw new InvalidParameterException("caseId == " + dto.getCaseId());
        builder.analysisCase(analysisCase);

        return builder.build();
    }

    private AnalysisExceptionRule getAsRuleDomain(AnalysisExceptionDTO dto) {

        AnalysisExceptionRule.AnalysisExceptionRuleBuilder builder = AnalysisExceptionRule.builder();

        // basic properties
        builder
                .id(dto.getId())
                .name(dto.getName())
                .type(AnalysisExceptions.RULE)
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
