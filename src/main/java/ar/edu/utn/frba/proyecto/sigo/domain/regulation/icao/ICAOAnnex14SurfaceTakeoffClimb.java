package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceTakeoffClimb extends ICAOAnnex14Surface {
    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.TAKEOFF_CLIMB.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.TAKEOFF_CLIMB.description();
    }

    private Double length;
    private Double slope;
    private Double finalWidth;
    private Double divergence;
    private Double distanceFromRunwayEnds;
    private Double lengthOfInnerEdge;
}
