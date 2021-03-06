package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

public enum ICAOAnnex14Surfaces {

    STRIP("Strip"),                                         // 0
    CONICAL("Conical"),                                     // 1
    INNER_HORIZONTAL("Inner Horizontal"),                   // 2
    INNER_APPROACH("Inner Approach"),                       // 3
    APPROACH("Approach"),                                   // 4
    APPROACH_FIRST_SECTION("Approach first section"),       // 5
    APPROACH_SECOND_SECTION("Approach second section"),     // 6
    APPROACH_HORIZONTAL_SECTION("Approach third section"),  // 7
    TRANSITIONAL("Transitional"),                           // 8
    INNER_TRANSITIONAL("Inner Transitional"),               // 9
    BALKED_LANDING_SURFACE("Balked Landing"),               // 10
    TAKEOFF_CLIMB("Take-off climb"),                        // 11
    OUTER_HORIZONTAL("Outer Horizontal");                   // 12

    private String description;

    ICAOAnnex14Surfaces(String s) {
        this.description = s;
    }

    public String description() {
        return description;
    }
}
