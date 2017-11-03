package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.dto.analysis.AnalysisObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import com.google.gson.Gson;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalysisObjectTranslator extends Translator<AnalysisObject, AnalysisObjectDTO>{

    @Inject
    public AnalysisObjectTranslator(
            Gson gson
    ){
        this.objectMapper = gson;
    }

    @Override
    public AnalysisObjectDTO getAsDTO(AnalysisObject domain) {
        return AnalysisObjectDTO.builder()
                .id(domain.getId())
                .caseId(domain.getAnalysisCase().getId())
                .objectId(domain.getPlacedObject().getId())
                .build();
    }

    @Override
    public AnalysisObject getAsDomain(AnalysisObjectDTO analysisObjectDTO) {
        throw new NotImplementedException();
    }
}
