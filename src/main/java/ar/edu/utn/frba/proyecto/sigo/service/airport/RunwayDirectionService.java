package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.*;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.icao.RunwayClassificationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.service.SigoService;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hibernate.SessionFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.inject.Inject;
import java.awt.*;

public class RunwayDirectionService extends SigoService<RunwayDirection, Runway> {

    @Inject
    public RunwayDirectionService(
            SessionFactory sessionFactory
    ){
        super(RunwayDirection.class, sessionFactory);
    }

    @Override
    protected void postCreateActions(RunwayDirection direction, Runway runway) {

        createApproachSection(direction);

        createTakeoffSection(direction, runway);

        createClassification(direction, runway);

        createStrip(direction);
    }

    @Override
    protected void preUpdateActions(RunwayDirection newInstance, RunwayDirection oldInstance){
        newInstance.setGeom(oldInstance.getGeom());
        newInstance.setStrip(oldInstance.getStrip());
        newInstance.setClassification(oldInstance.getClassification());
        newInstance.setApproachSection(oldInstance.getApproachSection());
        newInstance.setTakeoffSection(oldInstance.getTakeoffSection());
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

    public SimpleFeature getFeature(RunwayDirection direction) {

        return SimpleFeatureBuilder.build(
                getFeatureSchema(),
                new Object[]{
                        direction.getGeom(),
                        "Direction",
                        direction.getNumber().toString(),
                        direction.getPosition().position(),
                        direction.getAzimuth(),
                        direction.getHeight()
                },
                direction.getId().toString()
        );
    }

    private SimpleFeatureType getFeatureSchema() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

        tb.setName("Direction");
        tb.add("geom", Polygon.class, DefaultGeographicCRS.WGS84);
        tb.add("class", String.class);
        tb.add("number", String.class);
        tb.add("position", String.class);
        tb.add("azimuth", Double.class);
        tb.add("height", Double.class);

        return tb.buildFeatureType();
    }

    private void createStrip(RunwayDirection direction) {
        RunwayStrip strip = RunwayStrip.builder().build();

        direction.setStrip(strip);

        currentSession().persist(strip);
    }
}
