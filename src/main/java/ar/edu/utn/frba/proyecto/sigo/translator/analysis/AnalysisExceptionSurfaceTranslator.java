package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisExceptionSurface;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisExceptionSurfaceDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseService;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AnalysisExceptionSurfaceTranslator extends SigoTranslator<AnalysisExceptionSurface, AnalysisExceptionSurfaceDTO> {

    private final AnalysisCaseService caseService;

    @Inject
    public AnalysisExceptionSurfaceTranslator(
            Gson objectMapper,
            AnalysisCaseService caseService
    ) {
        super(objectMapper, AnalysisExceptionSurfaceDTO.class, AnalysisExceptionSurface.class);
        this.caseService = caseService;
    }

    @Override
    public AnalysisExceptionSurfaceDTO getAsDTO(AnalysisExceptionSurface domain) {
        return AnalysisExceptionSurfaceDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .caseId(domain.getAnalysisCase().getId())
                .heightAmls(domain.getHeightAmls())
                .geom(domain.getGeom())
                .build();
    }

    @Override
    public AnalysisExceptionSurface getAsDomain(AnalysisExceptionSurfaceDTO dto) {

        AnalysisExceptionSurface.AnalysisExceptionSurfaceBuilder builder = AnalysisExceptionSurface.builder();

        // basic properties
        builder
                .id(dto.getId())
                .name(dto.getName())
                .heightAmls(dto.getHeightAmls())
                .geom(dto.getGeom());

        // relation: case
        AnalysisCase analysisCase = caseService.get(dto.getCaseId());
        if(!Optional.ofNullable(analysisCase).isPresent())
            throw new InvalidParameterException("caseId == " + dto.getCaseId());
        builder.analysisCase(analysisCase);

        return builder.build();
    }
}
