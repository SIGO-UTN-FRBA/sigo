package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisSurfaceDTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Singleton;

@Singleton
public class AnalysisSurfaceTranslator extends Translator<AnalysisSurface, AnalysisSurfaceDTO>{

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
