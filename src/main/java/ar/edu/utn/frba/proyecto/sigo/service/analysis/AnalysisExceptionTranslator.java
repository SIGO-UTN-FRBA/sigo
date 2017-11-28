package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionRule;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionVisitor;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptions;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisExceptionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.util.Optional;

public class AnalysisExceptionTranslator extends Translator<AnalysisException, AnalysisExceptionDTO>{

    private AnalysisCaseService caseService;

    @Inject
    public AnalysisExceptionTranslator(
        Gson objectMapper,
        AnalysisCaseService caseService
    ){
        this.caseService = caseService;
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
                    .olsRuleId(exception.getOlsRuleId())
                    .property(exception.getProperty())
                    .value(exception.getValue())
                    .regulationId(exception.getRegulation().ordinal())
                    .build();
        }

        @Override
        public AnalysisExceptionDTO visitAnalysisExceptionSurface(AnalysisExceptionSurface exception) {
            return AnalysisExceptionDTO.builder()
                    .id(exception.getId())
                    .name(exception.getName())
                    .caseId(exception.getAnalysisCase().getId())
                    .properties(exception.getProperties())
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
            .properties(dto.getProperties());

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
                .property(dto.getProperty())
                .value(dto.getValue())
                .olsRuleId(dto.getOlsRuleId())
                .regulation(Regulations.values()[dto.getRegulationId()]);

        // relation: case
        AnalysisCase analysisCase = caseService.get(dto.getCaseId());
        if(!Optional.ofNullable(analysisCase).isPresent())
            throw new InvalidParameterException("caseId == " + dto.getCaseId());
        builder.analysisCase(analysisCase);

        return builder.build();
    }
}
