package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Analysis;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisDTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.time.ZoneOffset;

public class AnalysisTranslator extends Translator<Analysis, AnalysisDTO> {

    @Inject
    public AnalysisTranslator(
            Gson gson
    ){
        this.objectMapper = gson;
    }

    @Override
    public AnalysisDTO getAsDTO(Analysis domain) {
        return AnalysisDTO.builder()
                .id(domain.getId())
                .caseId(domain.getAnalysisCase().getId())
                .stageId(domain.getStage().ordinal())
                .statusId(domain.getStatus().ordinal())
                .creationDate(domain.getCreationDate().toInstant(ZoneOffset.UTC).toEpochMilli())
                .airportId(domain.getAnalysisCase().getAerodrome().getId())
                .build();
    }

    @Override
    public Analysis getAsDomain(AnalysisDTO analysisDTO) {
        return null;
    }
}
