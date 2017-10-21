package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

public enum ICAOAnnex14RunwayCodes {

    ONE("1"),       //0
    TWO("2"),       //1
    THREE("3"),     //2
    FOUR("4"),      //3
    ULM("ULM"),     //4
    AGRO("Agro");   //5

    private String code;

    ICAOAnnex14RunwayCodes(String code) {
        this.code = code;
    }

    public String description() {
        return code;
    }
}
