package ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa;

public enum FAARunwayCategories {
    NONE("None"),               // 0
    CATEGORY_I("CAT I"),        // 1
    CATEGORY_II("CAT II"),      // 2
    CATEGORY_III("CAT III");    // 3

    private String code;

    FAARunwayCategories(String s) {
        this.code = s;
    }

    public String code(){
        return this.code;
    }
}
