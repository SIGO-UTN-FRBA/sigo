package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.*;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.icao.RunwayClassificationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getAzimuth;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.getMiddle;
import static ar.edu.utn.frba.proyecto.sigo.utils.geom.GeometryHelper.sortDirectionCoordinates;

import static org.reflections.util.ConfigurationBuilder.build;

public class RunwayDirectionService extends SigoService<RunwayDirection, Runway> {

    @Inject
    public RunwayDirectionService(
            HibernateUtil hibernateUtil
    ){
        super(RunwayDirection.class, hibernateUtil.getSessionFactory());
    }

    @Override
    protected void postCreateActions(RunwayDirection direction, Runway runway) {

        createApproachSection(direction);

        createTakeoffSection(direction, runway);

        createClassification(direction, runway);
    }

    @Override
    protected void preUpdateActions(RunwayDirection newInstance, RunwayDirection oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
    }

    private void createTakeoffSection(RunwayDirection direction, Runway runway) {
        RunwayTakeoffSection takeoffSection = RunwayTakeoffSection.builder()
                .clearwayLength(0D)
                .clearwayWidth(runway.getWidth())
                .stopwayLength(0D)
                .runwayDirection(direction)
                .enabled(true)
                .build();

        currentSession().save(takeoffSection);
    }

    private void createApproachSection(RunwayDirection direction) {
        RunwayApproachSection approachSection = RunwayApproachSection.builder()
                .thresholdElevation(0D)
                .thresholdLength(0D)
                .runwayDirection(direction)
                .enabled(true)
                .build();

        currentSession().save(approachSection);
    }

    private void createClassification(RunwayDirection direction, Runway runway) {

        //TODO determinar clasificacion en base a la regulacion
        // runway.getAirport().getRegulation();

        RunwayClassificationICAOAnnex14 classification = RunwayClassificationICAOAnnex14.builder()
                .runwayDirection(direction)
                .build();

        currentSession().save(classification);
    }

}
