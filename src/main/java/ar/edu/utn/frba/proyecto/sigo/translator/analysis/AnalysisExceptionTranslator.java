package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisException;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisExceptionDTO;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RunwayDirectionService;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseService;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisExceptionTranslator extends SigoTranslator<AnalysisException, AnalysisExceptionDTO> {

    private AnalysisCaseService caseService;

    @Inject
    public AnalysisExceptionTranslator(
        Gson objectMapper,
        AnalysisCaseService caseService,
        OlsRuleICAOAnnex14Service ruleIcaoService,
        RunwayDirectionService directionService
    ){
        super(objectMapper,AnalysisExceptionDTO.class, AnalysisException.class);
        this.caseService = caseService;
        this.objectMapper = objectMapper;
        this.dtoClass = AnalysisExceptionDTO.class;
        this.domainClass = AnalysisException.class;
    }

    @Override
    public AnalysisExceptionDTO getAsDTO(AnalysisException domain) {
        return AnalysisExceptionDTO.builder()
                .id(domain.getId())
                .caseId(domain.getAnalysisCase().getId())
                .name(domain.getName())
                .typeId(domain.getType().ordinal())
                .build();
    }

    @Override
    public AnalysisException getAsDomain(AnalysisExceptionDTO dto) {

        throw new NotImplementedException();
    }
}
