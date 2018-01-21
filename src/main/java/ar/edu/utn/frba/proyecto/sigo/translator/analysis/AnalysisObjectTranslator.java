package ar.edu.utn.frba.proyecto.sigo.translator.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.service.analysis.AnalysisCaseService;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectService;
import com.google.gson.Gson;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AnalysisObjectTranslator extends SigoTranslator<AnalysisObject, AnalysisObjectDTO> {

    private AnalysisCaseService caseService;
    private PlacedObjectService placedObjectService;

    @Inject
    public AnalysisObjectTranslator(
            Gson gson,
            AnalysisCaseService caseService,
            PlacedObjectService placedObjectService
    ){
        super(gson, AnalysisObjectDTO.class, AnalysisObject.class);

        this.caseService = caseService;
        this.placedObjectService = placedObjectService;
    }

    @Override
    public AnalysisObjectDTO getAsDTO(AnalysisObject domain) {
        return AnalysisObjectDTO.builder()
                .id(domain.getId())
                .caseId(domain.getAnalysisCase().getId())
                .objectId(domain.getElevatedObject().getId())
                .objectTypeId(domain.getElevatedObject().getType().ordinal())
                .included(domain.getIncluded())
                .build();
    }

    @Override
    public AnalysisObject getAsDomain(AnalysisObjectDTO dto) {
        AnalysisObject.AnalysisObjectBuilder builder = AnalysisObject.builder();

        // basic attributes
        builder
                .id(dto.getId())
                .included(dto.getIncluded());

        // relation: object
        Optional<PlacedObject> placedObject = Optional.ofNullable(placedObjectService.get(dto.getObjectId()));
        if(!placedObject.isPresent())
            throw new InvalidParameterException("objectId");
        builder.elevatedObject(placedObject.get());

        // relation: case
        Optional<AnalysisCase> analysisCase = Optional.ofNullable(caseService.get(dto.getCaseId()));
        if(!analysisCase.isPresent())
            throw new InvalidParameterException("caseId");
        builder.analysisCase(analysisCase.get());

        return builder.build();
    }
}
