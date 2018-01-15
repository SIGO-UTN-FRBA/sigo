package ar.edu.utn.frba.proyecto.sigo.domain.object;

public enum ElevatedObjectTypes {

    BUILDING("Building"),
    INDIVIDUAL("Individual"),
    OVERHEAD_WIRED("Overhead wired"),
    LEVEL_CURVE("Level curve");

    private String type;

    ElevatedObjectTypes(String s) {
        this.type = s;
    }

    public String type() {
        return type;
    }
}
