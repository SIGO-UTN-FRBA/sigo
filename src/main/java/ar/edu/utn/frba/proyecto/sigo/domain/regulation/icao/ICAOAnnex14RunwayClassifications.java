package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

public enum ICAOAnnex14RunwayClassifications {

    NON_INSTRUMENT("NI"),           // 0
    NON_PRECISION_APPROACH("NP"),   // 1
    PRECISION_APPROACH("P");        // 2

    private String code;

    ICAOAnnex14RunwayClassifications(String s) {
        this.code = s;
    }


    public String code() {
        return code;
    }

}
