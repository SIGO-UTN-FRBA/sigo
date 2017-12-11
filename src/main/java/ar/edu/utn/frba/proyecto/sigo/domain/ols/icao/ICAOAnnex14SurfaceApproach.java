package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceApproach extends ICAOAnnex14Surface {

    private Double lengthOfInnerEdge;
    private Double distanceFromThreshold;
    private Double divergence;

    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.APPROACH.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.APPROACH.description();
    }
}
