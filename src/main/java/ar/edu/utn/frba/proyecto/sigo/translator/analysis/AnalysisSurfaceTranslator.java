package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisSurfaceDTO;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;
import org.apache.commons.lang.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisSurfaceTranslator extends SigoTranslator<AnalysisSurface, AnalysisSurfaceDTO> {

    @Inject
    public AnalysisSurfaceTranslator(Gson objectMapper) {
        super(objectMapper, AnalysisSurfaceDTO.class, AnalysisSurface.class);
    }

    @Override
    public AnalysisSurfaceDTO getAsDTO(AnalysisSurface domain) {
        return AnalysisSurfaceDTO.builder()
                .id(domain.getId())
                .caseId(domain.getAnalysisCase().getId())
                .directionId(domain.getDirection().getId())
                .surface(domain.getSurface())
                .build();
    }

    @Override
    public AnalysisSurface getAsDomain(AnalysisSurfaceDTO analysisSurfaceDTO) {
        throw new NotImplementedException();
    }
}
