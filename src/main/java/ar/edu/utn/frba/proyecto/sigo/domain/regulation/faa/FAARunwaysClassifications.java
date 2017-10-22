package ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa;

public enum FAARunwaysClassifications {

    NONE("None"),               // 0
    CATEGORY_I("CAT I"),        // 1
    CATEGORY_II("CAT II"),      // 2
    CATEGORY_III("CAT III");    // 3

    private String description;

    FAARunwaysClassifications(String s) {
        this.description = s;
    }

    public String description() {
        return description;
    }
}
