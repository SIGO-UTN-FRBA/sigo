package ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa;

public enum FAARunwayClassifications {

    NON_INSTRUMENT("NI"),           // 0
    NON_PRECISION_APPROACH("NP"),   // 1
    PRECISION_APPROACH("P");        // 2

    private String code;

    FAARunwayClassifications(String s) {
        this.code = s;
    }


    public String code() {
        return code;
    }
}
