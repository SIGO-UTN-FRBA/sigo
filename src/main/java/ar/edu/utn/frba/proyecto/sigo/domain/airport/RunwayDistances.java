package ar.edu.utn.frba.proyecto.sigo.domain.airport;

public enum RunwayDistances {

    TORA("TORA"),
    TODA("TODA"),
    ASDA("ASDA"),
    LDA("LDA");

    private String distance;

    RunwayDistances(String s) {
        this.distance = s;
    }

    public String shortName(){return this.distance;}

    public String largeName(){
        switch (this.ordinal()) {
            case 0 : return "Takeoff Run Available";
            case 1 : return "Takeoff Distance Available";
            case 2 : return "Accelerate-Stop Distance Available";
            case 3 : return "Landing Distance Available";
            default: return null;
        }
    }

    public String description(){
        switch (this.ordinal()) {
            case 0 : return "The runway length declared available and suitable for the ground run of an airplane taking off.";
            case 1 : return "The length of the TORA plus the length of the clearway, if provided.";
            case 2 : return "The TORA plus the length of the stopway, if provided for the acceleration and deceleration of an airplane aborting a takeoff.";
            case 3 : return "The runway length declared available and suitable for landing an airplane.";
            default: return null;
        }
    }
}
