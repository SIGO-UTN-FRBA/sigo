package ar.edu.utn.frba.proyecto.sigo.domain.airport;

public enum RunwaySurfaces {
    ASP("Asphalt"),
    BIT("Bitumenous asphalt or tarmac"),
    BRI("Bricks (no longer in use)"),
    CLA("Clay"),
    COM("Composite"),
    CON("Concrete"),
    COP("Composite"),
    COR("Coral (fine crushed coral reef structures)"),
    GRE("Graded or rolled earth"),
    GRS("Grass or earth not graded or rolled"),
    GVL("Gravel"),
    ICE("Ice"),
    LAT("Laterite"),
    MAC("Macadam"),
    PC("Partially concrete"),
    PS("Permanent surface"),
    PSP("Marston Matting (derived from pierced/perforated steel planking)"),
    SAN("Sand"),
    SMT("Sommerfeld Tracking"),
    SNO("Snow"),
    U("Unknown surface");


    private String description;

    RunwaySurfaces(String s) {
        this.description = s;
    }


    public String description() {
        return description;
    }
}
