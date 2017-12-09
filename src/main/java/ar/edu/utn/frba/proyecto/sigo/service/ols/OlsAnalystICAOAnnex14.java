package ar.edu.utn.frba.proyecto.sigo.service.ols;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.icao.RunwayClassificationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisCase;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObject;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisObstacle;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;
import ar.edu.utn.frba.proyecto.sigo.service.regulation.OlsRuleICAOAnnex14Service;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OlsAnalystICAOAnnex14 extends OlsAnalyst {

    private OlsRuleICAOAnnex14Service service;

    @Inject
    public OlsAnalystICAOAnnex14(
            OlsRuleICAOAnnex14Service service,
            @Assisted AnalysisCase analysisCase
    ) {
        super(analysisCase);

        this.service = service;
    }

    @Override
    protected void applyExceptions() {

    }

    @Override
    protected void defineObstacles() {

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
                .map( object -> AnalysisObstacle.builder()
                                    .object(object)
                                    .surface(surface)
                                    .build()
                )
                .collect(Collectors.toSet());
    }

    private boolean isObstacle(AnalysisSurface surface, AnalysisObject object) {

        //TODO intersectar en altura (evaluando propiedades)

        return surface.getGeometry().covers(object.getPlacedObject().getGeom());
    }

    @Override
    protected void initializeSurfaces() {

        List<AnalysisSurface> surfaces = createAnalysisSurfaces();

        new ICAOAnnex14SurfaceGeometriesHelper(this.analysisCase.getAerodrome(), surfaces)
                .setupGeometries();

        this.analysisCase.setSurfaces(surfaces);
    }


    private List<AnalysisSurface> createAnalysisSurfaces() {

        return this.analysisCase.getAerodrome().getRunways()
                .stream()
                .map(Runway::getDirections)
                .flatMap(Collection::stream)
                .map(this::createAnalysisSurfaces)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<AnalysisSurface> createAnalysisSurfaces(RunwayDirection direction){

        RunwayClassificationICAOAnnex14 classification = (RunwayClassificationICAOAnnex14) direction.getClassification();

        return this.service
                .getSurfaces(classification.getRunwayClassification(), classification.getRunwayCategory(), classification.getRunwayTypeNumber(), false)
                .stream()
                .map(s -> AnalysisSurface.builder()
                            .analysisCase(this.analysisCase)
                            .surface(s)
                            .direction(direction)
                            .build()
                )
                .collect(Collectors.toList());
    }
}
