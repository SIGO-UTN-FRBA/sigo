package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceConical extends ICAOAnnex14Surface {

    private Double slope;
    private Double height;
}
