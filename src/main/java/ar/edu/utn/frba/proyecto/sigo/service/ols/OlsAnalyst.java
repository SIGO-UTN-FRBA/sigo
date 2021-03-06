package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.*;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.Comparator;
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

        determineValidityOfObstacles();

        getCurrentSession().save(analysisCase);
    }

    private void determineValidityOfObstacles() {

        analysisCase.getObjects()
                .stream()
                .map(object -> analysisCase.getObstaclesByObject(object))
                .map(obstacles -> obstacles.max(Comparator.comparingDouble(AnalysisObstacle::getPenetration)))
                .forEach( maybe -> maybe.ifPresent( obstacle -> obstacle.setIsValid(true)));
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
                .forEach(os -> os.setIsExcepted(true));
    }

    protected abstract void initializeSurfaces();

    private void defineObstacles() {

        this.getAnalysisCase().getRestrictions()
                .map(this::defineObstacles)
                .flatMap(Collection::stream)
                .forEach( o -> this.analysisCase.addObstacle(o));

    }

    private Set<AnalysisObstacle> defineObstacles(AnalysisRestriction restriction) {
        return this.getAnalysisCase().getObjects()
                .stream()
                .filter(AnalysisObject::getIncluded)
                .filter(restriction::isObstacle)
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
                .isExcepted(false)
                .isValid(false)
                .build();

        if(analysisObstacle.getPenetration() < 0D )
            analysisObstacle.setResult(createSuggestedAnalystResultForSafetyObject(analysisObstacle));
 //       else if(analysisObstacle.getPenetration() > 0D && analysisObject.getElevatedObject().getType().equals(ElevatedObjectTypes.LEVEL_CURVE))
 //           analysisObstacle.setResult(createSuggestedAnalystResultForFixedRiskyObject(analysisObstacle));

        return analysisObstacle;
    }

    private AnalysisResult createSuggestedAnalystResultForSafetyObject(AnalysisObstacle analysisObstacle) {


        return AnalysisResult.builder()
                .obstacle(analysisObstacle)
                .hasAdverseEffect(false)
                .allowed(true)
                .extraDetail("This result was generated automatically.")
                .build();
    }
/*
    private AnalysisResult createSuggestedAnalystResultForFixedRiskyObject(AnalysisObstacle analysisObstacle){

        return AnalysisResult.builder()
                .obstacle(analysisObstacle)
                .hasAdverseEffect(true)
                .aspect()
                .allowed(true)
                .extraDetail("This result was generated automatically.")
                .build();
    }
*/
    public abstract Double determineHeightForAnalysisSurface(AnalysisSurface analysisSurface, Point point);
}
