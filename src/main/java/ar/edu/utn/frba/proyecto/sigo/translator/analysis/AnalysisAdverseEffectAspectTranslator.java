package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisAdverseEffectAspect;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisAdverseEffectAspectDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisAdverseEffectAspectTranslator extends SigoTranslator<AnalysisAdverseEffectAspect, AnalysisAdverseEffectAspectDTO> {

    @Inject
    public AnalysisAdverseEffectAspectTranslator(Gson objectMapper) {
        super(objectMapper, AnalysisAdverseEffectAspectDTO.class, AnalysisAdverseEffectAspect.class);
    }

    @Override
    public AnalysisAdverseEffectAspectDTO getAsDTO(AnalysisAdverseEffectAspect domain) {
        return AnalysisAdverseEffectAspectDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .build();
    }

    @Override
    public AnalysisAdverseEffectAspect getAsDomain(AnalysisAdverseEffectAspectDTO dto) {
        throw new NotImplementedException();
    }
}
