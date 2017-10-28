package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceInnerHorizontal extends ICAOAnnex14Surface {

    private Double height;
    private Double radius;
}
