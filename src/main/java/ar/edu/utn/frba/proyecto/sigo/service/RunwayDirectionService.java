package ar.edu.utn.frba.proyecto.sigo.service;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Runway;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayApproachSection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayTakeoffSection;
import ar.edu.utn.frba.proyecto.sigo.persistence.HibernateUtil;

import javax.inject.Inject;

public class RunwayDirectionService extends SigoService<RunwayDirection, Runway>{

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
