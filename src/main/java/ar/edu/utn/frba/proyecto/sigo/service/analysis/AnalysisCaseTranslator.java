package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisCaseDTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import com.google.gson.Gson;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.Optional;

public class AnalysisCaseTranslator extends Translator<AnalysisCase, AnalysisCaseDTO> {

    @Inject
    public AnalysisCaseTranslator(
            Gson gson
    ){
        this.objectMapper = gson;
    }

    @Override
    public AnalysisCaseDTO getAsDTO(AnalysisCase domain) {

        AnalysisCaseDTO.AnalysisCaseDTOBuilder builder = AnalysisCaseDTO.builder();

        builder
                .id(domain.getId())
                .airportId(domain.getAerodrome().getId())
                .status(domain.getStatus().ordinal())
                .stage(domain.getStage().ordinal())
                .regulationId(domain.getRegulation().ordinal())
                .creationDate(domain.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli());

        Optional.ofNullable(domain.getArea()).ifPresent(a -> builder.areaId(a.getId()));

        return builder.build();
    }

    @Override
    public AnalysisCase getAsDomain(AnalysisCaseDTO analysisCaseDTO) {
        throw new NotImplementedException();
    }
}
