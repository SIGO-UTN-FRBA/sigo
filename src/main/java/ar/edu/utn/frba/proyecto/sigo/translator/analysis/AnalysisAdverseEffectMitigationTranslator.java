package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisAdverseEffectMitigation;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisAdverseEffectMitigationDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisAdverseEffectMitigationTranslator extends SigoTranslator<AnalysisAdverseEffectMitigation,AnalysisAdverseEffectMitigationDTO> {

    @Inject
    public AnalysisAdverseEffectMitigationTranslator(Gson objectMapper) {
        super(objectMapper, AnalysisAdverseEffectMitigationDTO.class, AnalysisAdverseEffectMitigation.class);
    }

    @Override
    public AnalysisAdverseEffectMitigationDTO getAsDTO(AnalysisAdverseEffectMitigation domain) {
        return AnalysisAdverseEffectMitigationDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .operationDamage(domain.getOperationDamage())
                .aspectId(domain.getAspect().getId())
                .build();
    }

    @Override
    public AnalysisAdverseEffectMitigation getAsDomain(AnalysisAdverseEffectMitigationDTO dto) {
        throw new NotImplementedException();
    }
}
