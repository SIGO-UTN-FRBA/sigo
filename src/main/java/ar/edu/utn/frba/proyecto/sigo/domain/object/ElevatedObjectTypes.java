package ar.edu.utn.frba.proyecto.sigo.domain.object;

public enum ElevatedObjectTypes {

    BUILDING("building", "Building"),               //0
    INDIVIDUAL("individual", "Individual"),         //1
    OVERHEAD_WIRED("wired", "Overhead wired"),      //2
    LEVEL_CURVE("curve","Level curve"),             //3
    TRACK_SECTION("trackSection", "Tack section");  //4

    private String code;
    private String description;

    ElevatedObjectTypes(String code, String description) {
        this.code = code;
        this.description= description;
    }

    public String code() {
        return code;
    }
    public String description() {
        return description;
    }
}
