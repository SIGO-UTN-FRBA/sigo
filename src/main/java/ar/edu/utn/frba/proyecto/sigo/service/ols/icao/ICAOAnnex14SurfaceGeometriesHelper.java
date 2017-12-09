package ar.edu.utn.frba.proyecto.sigo.service.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.AnalysisSurface;

import java.util.List;

public class ICAOAnnex14SurfaceGeometriesHelper {

    private Airport airport;
    private List<AnalysisSurface> surfaces;

    public ICAOAnnex14SurfaceGeometriesHelper(Airport airport, List<AnalysisSurface> surfaces) {
        this.airport = airport;
        this.surfaces = surfaces;
    }

    public void setupGeometries(){
        //TODO create geometries for each surface

    }
}
