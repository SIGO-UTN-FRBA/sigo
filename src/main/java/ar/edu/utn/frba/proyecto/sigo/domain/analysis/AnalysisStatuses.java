package ar.edu.utn.frba.proyecto.sigo.domain.analysis;

public enum AnalysisStatuses {

    INITIALIZED("Initialized"),
    IN_PROGRESS("In Progress"),
    FINISHED("Finished"),
    CANCELLED("Cancelled");

    String description;

    AnalysisStatuses(String description) {
        this.description=description;
    }

    public String description() {
        return description;
    }
}
