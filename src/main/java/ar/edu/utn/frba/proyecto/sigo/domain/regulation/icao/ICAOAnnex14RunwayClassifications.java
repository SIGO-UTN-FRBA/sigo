package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

public enum ICAOAnnex14RunwayClassifications {

    NON_INSTRUMENT("Non-Instrumental Approach"),            // 0
    NON_PRECISION_APPROACH("Non-Precision Approach"),       // 1
    PRECISION_APPROACH("Precision Approach");               // 2

    private String code;

    ICAOAnnex14RunwayClassifications(String s) {
        this.code = s;
    }


    public String description() {
        return code;
    }

}
