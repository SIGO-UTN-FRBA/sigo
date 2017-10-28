package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceApproach extends ICAOAnnex14Surface {

    private Double lengthOfInnerEdge;
    private Double distanceFromThreshold;
    private Double divergence;
}
