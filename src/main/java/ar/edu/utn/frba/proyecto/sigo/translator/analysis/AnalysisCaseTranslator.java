package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisCaseDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisCaseTranslator extends SigoTranslator<AnalysisCase, AnalysisCaseDTO> {

    @Inject
    public AnalysisCaseTranslator(
            Gson gson
    ){
        super(gson, AnalysisCaseDTO.class, AnalysisCase.class);
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
