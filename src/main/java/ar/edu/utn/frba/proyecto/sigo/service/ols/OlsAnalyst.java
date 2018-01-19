package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.object.ElevatedObject;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public abstract class OlsAnalyst {

    protected AnalysisCase analysisCase;
    protected SessionFactory sessionFactory;

    public OlsAnalyst(AnalysisCase analysisCase, SessionFactory sessionFactory) {
        this.analysisCase = analysisCase;
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public void analyze(){

        initializeSurfaces();

        defineObstacles();

        //applyExceptions();

        getCurrentSession().save(analysisCase);
    }

    protected abstract void applyExceptions();

    protected abstract void initializeSurfaces();


    private void defineObstacles() {

        Set<AnalysisObstacle> obstacles = this.getAnalysisCase().getSurfaces()
                .stream()
                .map(this::defineObstacles)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        this.analysisCase.setObstacles(obstacles);
    }

    private Set<AnalysisObstacle> defineObstacles(AnalysisSurface surface) {
        return this.getAnalysisCase().getObjects()
                .stream()
                .filter(object -> isObstacle(surface, object))
                .map(object -> createAnalysisObstacle(surface, object))
                .collect(Collectors.toSet());
    }


    protected AnalysisObstacle createAnalysisObstacle(AnalysisSurface surface, AnalysisObject analysisObject) {

        Double surfaceHeight = determineSurfaceHeight(surface, analysisObject.getElevatedObject());

        Double objectHeight = analysisObject.getElevatedObject().getHeightAmls();

        return AnalysisObstacle.builder()
                .object(analysisObject)
                .surface(surface)
                .analysisCase(this.getAnalysisCase())
                .objectHeight(objectHeight)
                .surfaceHeight(surfaceHeight)
                .build();
    }

    protected abstract Double determineSurfaceHeight(AnalysisSurface analysisSurface, ElevatedObject object);

    protected Boolean isObstacle(AnalysisSurface surface, AnalysisObject analysisObject) {

        Geometry surfaceGeometry = surface.getSurface().getGeometry();

        ElevatedObject object = analysisObject.getElevatedObject();

        return surfaceGeometry.intersects(object.getGeom());
        //&& ( !object.getType().equals(ElevatedObjectTypes.LEVEL_CURVE)
        //        || object.getHeightAmls() >= determineSurfaceHeight(surface, object)
        //);
    }
}
