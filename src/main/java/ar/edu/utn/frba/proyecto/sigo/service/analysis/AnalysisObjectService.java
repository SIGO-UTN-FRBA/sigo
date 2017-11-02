package ar.edu.utn.frba.proyecto.sigo.service.analysis;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AnalysisObjectService extends SigoService<AnalysisObject, AnalysisCase>{

    @Inject
    public AnalysisObjectService(
            HibernateUtil hibernateUtil
    ) {
        super(AnalysisObject.class, hibernateUtil.getSessionFactory());
    }

    public List<AnalysisObject> updateAnalysedObjects(AnalysisCase analysisCase){

        throw new NotImplementedException();
    }
}
