package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.*;
import ar.edu.utn.frba.proyecto.sigo.domain.object.ElevatedObjectTypes;
import com.vividsolutions.jts.geom.Point;
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

        applyExceptions();

        getCurrentSession().save(analysisCase);
    }

    protected void applyExceptions(){
        this.analysisCase.getSurfaceExceptions().forEach(this::applyException);
    }

    private void applyException(AnalysisExceptionSurface exception){

        Set<AnalysisObstacle> obstaclesCausedByException = analysisCase.getObstaclesCausedByRestriction(exception);

        analysisCase.getSurfaces()
                .stream()
                .map(s -> analysisCase.getObstaclesCausedByRestriction(s))
                .flatMap(Collection::stream)
                .filter(os -> obstaclesCausedByException.stream().anyMatch(oe -> oe.getObject().equals(os.getObject())))
                .forEach(os -> os.setExcepting(true));
    }

    protected abstract void initializeSurfaces();

    private void defineObstacles() {

        Set<AnalysisObstacle> analysisObstacles = this.getAnalysisCase().getRestrictions()
                .map(this::defineObstacles)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        this.analysisCase.setObstacles(analysisObstacles);
    }

    private Set<AnalysisObstacle> defineObstacles(AnalysisRestriction restriction) {
        return this.getAnalysisCase().getObjects()
                .stream()
                .filter(object -> object.getIncluded() && isObstacle(restriction, object))
                .map(object -> createAnalysisObstacle(restriction, object))
                .collect(Collectors.toSet());
    }


    protected AnalysisObstacle createAnalysisObstacle(AnalysisRestriction restriction, AnalysisObject analysisObject) {

        Point intersection = restriction.getGeometry()
                .intersection(analysisObject.getGeometry())
                .getInteriorPoint();

        Double restrictionHeight = restriction.determineHeightAt(intersection, this);

        Double objectHeight = analysisObject.getElevatedObject().getHeightAmls();

        AnalysisObstacle analysisObstacle = AnalysisObstacle.builder()
                .object(analysisObject)
                .restriction(restriction)
                .analysisCase(this.getAnalysisCase())
                .objectHeight(objectHeight)
                .restrictionHeight(restrictionHeight)
                .excepting(false)
                .build();

        if(analysisObstacle.getPenetration() < 0D )
            analysisObstacle.setResult(createSuggestedAnalystResultForSafetyObject(analysisObstacle));
        else if(analysisObstacle.getPenetration() > 0D && analysisObject.getElevatedObject().getType().equals(ElevatedObjectTypes.LEVEL_CURVE))
            analysisObstacle.setResult(createSuggestedAnalystResultForFixedRiskyObject(analysisObstacle));

        return analysisObstacle;
    }

    private AnalysisResult createSuggestedAnalystResultForSafetyObject(AnalysisObstacle analysisObstacle) {

        //FIXME
        AnalysisResultReason reason = getCurrentSession().get(AnalysisResultReason.class, 7L);

        return AnalysisResult.builder()
                .obstacle(analysisObstacle)
                .reason(reason)
                .mustKeep(true)
                .isObstacle(false)
                .reasonDetail("This reason was generated automatically.")
                .build();
    }

    private AnalysisResult createSuggestedAnalystResultForFixedRiskyObject(AnalysisObstacle analysisObstacle){

        //FIXME
        AnalysisResultReason reason = getCurrentSession().get(AnalysisResultReason.class, 1L);

        return AnalysisResult.builder()
                .obstacle(analysisObstacle)
                .reason(reason)
                .mustKeep(true)
                .isObstacle(true)
                .reasonDetail("This reason was generated automatically.")
                .build();
    }

    public abstract Double determineHeightForAnalysisSurface(AnalysisSurface analysisSurface, Point point);

    protected Boolean isObstacle(AnalysisRestriction restriction, AnalysisObject analysisObject) {

        return restriction.getGeometry().intersects(analysisObject.getGeometry());
    }
}
