package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceBalkedLanding extends ICAOAnnex14Surface {

    private Double lengthOfInnerEdge;
    private Double distanceFromThreshold;
    private Double divergence;
    private Double slope;

    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.BALKED_LANDING_SURFACE.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.BALKED_LANDING_SURFACE.description();
    }
}
