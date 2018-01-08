package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisCaseDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.Translator;
import com.google.gson.Gson;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;

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
                .regulationId(domain.getRegulation().ordinal())
                .searchRadius(domain.getSearchRadius());

        return builder.build();
    }

    @Override
    public AnalysisCase getAsDomain(AnalysisCaseDTO analysisCaseDTO) {
        throw new NotImplementedException();
    }
}
