package ar.edu.utn.frba.proyecto.sigo.domain.object;

public enum PlacedObjectTypes {

    BUILDING("Building"),
    INDIVIDUAL("Individual"),
    OVERHEAD_WIRED("Overhead wired");

    private String type;

    PlacedObjectTypes(String s) {
        this.type = s;
    }

    public String type() {
        return type;
    }
}
