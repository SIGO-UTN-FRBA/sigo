package ar.edu.utn.frba.proyecto.sigo.domain.object;

public enum TrackTypes {
    ROAD("Road"),           //0
    HIGHWAY("Highway"),     //1
    RAILWAY("Railway");     //2

    public String description() {
        return description;
    }

    private String description;

    TrackTypes(String description) {
        this.description = description;
    }


}
