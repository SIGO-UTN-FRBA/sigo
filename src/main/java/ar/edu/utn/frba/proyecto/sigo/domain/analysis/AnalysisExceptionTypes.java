package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

public enum AnalysisExceptionTypes {
    SURFACE("Surface"),                 //0
    RULE("Rule"),                       //1
    DYNAMIC_SURFACE("Dynamic Surface"); //2

    private String description;

    AnalysisExceptionTypes(String d) {
        this.description = d;
    }

    public String description() {
        return description;
    }
}
