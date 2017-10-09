package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;

public class RunwayDistanceHelper {

    public static Double calculateTORALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        return runway;
    }

    public static Double calculateTODALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double clearway = direction.getTakeoffSection().getClearwayLength();

        return runway +  clearway;
    }

    public static Double calculateASDALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double stopway = direction.getTakeoffSection().getStopwayLength();

        return runway + stopway;
    }

    public static Double calculateLDALength(RunwayDirection direction){

        Double runway = direction.getRunway().getLength();

        Double threshold = direction.getApproachSection().getThresholdLength();

        return runway - threshold;
    }
}
