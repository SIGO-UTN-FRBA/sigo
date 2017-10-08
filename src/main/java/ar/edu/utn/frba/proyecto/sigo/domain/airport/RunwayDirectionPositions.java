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

    public static RunwayDirectionPositions getEnum(Integer code) {

        switch (code){
            case 0:
                return NONE;
            case 1:
                return LEFT;
            case 2:
                return CENTER;
            case 3:
                return RIGHT;
            default:
                return null;
        }
    }
}
