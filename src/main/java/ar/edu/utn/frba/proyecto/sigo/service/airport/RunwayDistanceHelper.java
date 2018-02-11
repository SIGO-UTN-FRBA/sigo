package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;

import java.util.Optional;

public class RunwayDistanceHelper {

    public static Double calculateTORALength(RunwayDirection direction){

        return direction.getRunway().getLength();
    }

    public static Double calculateTODALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double clearway = Optional.ofNullable(direction.getTakeoffSection().getClearwayLength()).orElse(0D);

        return runway +  clearway;
    }

    public static Double calculateASDALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double stopway = Optional.ofNullable(direction.getTakeoffSection().getStopwayLength()).orElse(0D);

        return runway + stopway;
    }

    public static Double calculateLDALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double threshold = Optional.ofNullable(direction.getApproachSection().getThresholdLength()).orElse(0D);

        return runway - threshold;
    }
}
