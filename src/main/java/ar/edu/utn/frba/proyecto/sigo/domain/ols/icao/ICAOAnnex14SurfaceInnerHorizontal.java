package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceInnerHorizontal extends ICAOAnnex14Surface {

    private Double height;
    private Double radius;

    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.INNER_HORIZONTAL.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.INNER_HORIZONTAL.description();
    }
}
