package ar.edu.utn.frba.proyecto.sigo.domain.object;

public enum ElevatedObjectTypes {

    BUILDING("building", "Building"),
    INDIVIDUAL("individual", "Individual"),
    OVERHEAD_WIRED("wired", "Overhead wired"),
    LEVEL_CURVE("curve","Level curve");

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
