package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.analysis.*;
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
//        this.analysisCase.getSurfaces()
//                .stream()
//                .filter(s -> s.getGeometry().intersects(exception.getGeometry()))
//                .forEach(s -> redefineSurface(s, exception));
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
                .filter(object -> isObstacle(restriction, object))
                .map(object -> createAnalysisObstacle(restriction, object))
                .collect(Collectors.toSet());
    }


    protected AnalysisObstacle createAnalysisObstacle(AnalysisRestriction restriction, AnalysisObject analysisObject) {

        Point intersection = restriction.getGeometry()
                .intersection(analysisObject.getGeometry())
                .getInteriorPoint();

        Double restrictionHeight = restriction.determineHeightAt(intersection, this);

        Double objectHeight = analysisObject.getElevatedObject().getHeightAmls();

        return AnalysisObstacle.builder()
                .object(analysisObject)
                .restriction(restriction)
                .analysisCase(this.getAnalysisCase())
                .objectHeight(objectHeight)
                .restrictionHeight(restrictionHeight)
                .build();
    }

    public abstract Double determineHeightForAnalysisSurface(AnalysisSurface analysisSurface, Point point);

    protected Boolean isObstacle(AnalysisRestriction restriction, AnalysisObject analysisObject) {

        return restriction.getGeometry().intersects(analysisObject.getGeometry());
    }
}
