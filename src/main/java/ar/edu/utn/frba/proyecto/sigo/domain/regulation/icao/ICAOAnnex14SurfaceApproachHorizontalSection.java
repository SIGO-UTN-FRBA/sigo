package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceApproachHorizontalSection extends ICAOAnnex14Surface {

    private Double length;
    private Double totalLength;
}
