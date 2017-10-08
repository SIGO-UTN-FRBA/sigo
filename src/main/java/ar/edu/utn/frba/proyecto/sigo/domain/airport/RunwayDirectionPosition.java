package ar.edu.utn.frba.proyecto.sigo.domain.airport;

public enum RunwayDirectionPosition {

    NONE(""),
    LEFT ("L"),
    CENTER("C"),
    RIGHT ("R");


    private final String position;

    RunwayDirectionPosition(String position){
        this.position = position;
    }

    public String position(){
        return position;
    }

    public static RunwayDirectionPosition getEnum(Integer code) {

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
