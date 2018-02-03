package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.ZoneOffset;

@Singleton
public class AnalysisTranslator extends SigoTranslator<Analysis, AnalysisDTO> {

    @Inject
    public AnalysisTranslator(
            Gson gson
    ){
        super(gson, AnalysisDTO.class, Analysis.class);
    }

    @Override
    public AnalysisDTO getAsDTO(Analysis domain) {
        return AnalysisDTO.builder()
                .id(domain.getId())
                .caseId(domain.getAnalysisCase().getId())
                .stageId(domain.getStage().ordinal())
                .statusId(domain.getStatus().ordinal())
                .creationDate(domain.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli())
                .editionDate(domain.getEditionDate().toInstant(ZoneOffset.UTC).toEpochMilli())
                .airportId(domain.getAnalysisCase().getAerodrome().getId())
                .regulationId(domain.getRegulation().ordinal())
                .userId(domain.getUser().getId())
                .userNickname(domain.getUser().getNickname())
                .build();
    }

    @Override
    public Analysis getAsDomain(AnalysisDTO analysisDTO) {
        throw new NotImplementedException();
    }
}
