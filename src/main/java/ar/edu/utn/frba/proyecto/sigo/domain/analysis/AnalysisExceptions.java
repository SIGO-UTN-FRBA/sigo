package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

public enum AnalysisExceptions {
    SURFACE("Surface"),     //0
    RULE("Rule");           //1

    private String description;

    AnalysisExceptions(String d) {
        this.description = d;
    }

    public String description() {
        return description;
    }
}
