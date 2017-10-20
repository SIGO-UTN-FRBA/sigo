package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.*;
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

public class RunwayDirectionService extends SigoService<RunwayDirection, Runway> {

    @Inject
    public RunwayDirectionService(
            HibernateUtil hibernateUtil
    ){
        super(RunwayDirection.class, hibernateUtil.getSessionFactory());
    }

    @Override
    protected void postCreateActions(RunwayDirection direction, Runway ruwnay) {

        createSections(direction, ruwnay);
    }

    @Override
    protected void preUpdateActions(RunwayDirection newInstance, RunwayDirection oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
    }

    private void createSections(RunwayDirection direction, Runway ruwnay) {
        RunwayApproachSection approachSection = RunwayApproachSection.builder()
                .thresholdElevation(0D)
                .thresholdLength(0D)
                .runwayDirection(direction)
                .enabled(true)
                .build();

        currentSession().save(approachSection);

        RunwayTakeoffSection takeoffSection = RunwayTakeoffSection.builder()
                .clearwayLength(0D)
                .clearwayWidth(ruwnay.getWidth())
                .stopwayLength(0D)
                .runwayDirection(direction)
                .enabled(true)
                .build();

        currentSession().save(takeoffSection);
    }

}
