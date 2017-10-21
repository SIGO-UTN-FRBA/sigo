package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceInnerApproach extends ICAOAnnex14Surface{

    private Double width;
    private Double distanceFromThreshold;
    private Double length;
    private Double slope;
}
