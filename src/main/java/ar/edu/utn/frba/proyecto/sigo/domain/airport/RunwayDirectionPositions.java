package ar.edu.utn.frba.proyecto.sigo.domain.airport;

public enum RunwayDirectionPositions {

    NONE(""),
    LEFT ("L"),
    CENTER("C"),
    RIGHT ("R");

    private final String position;

    RunwayDirectionPositions(String position){
        this.position = position;
    }

    public String position(){
        return position;
    }
}
