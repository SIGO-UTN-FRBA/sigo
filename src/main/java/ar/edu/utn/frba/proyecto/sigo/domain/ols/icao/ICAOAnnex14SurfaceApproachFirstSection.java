package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceApproachFirstSection extends ICAOAnnex14Surface {

    private Double length;
    private Double slope;

    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.APPROACH_FIRST_SECTION.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.APPROACH_FIRST_SECTION.description();
    }
}
