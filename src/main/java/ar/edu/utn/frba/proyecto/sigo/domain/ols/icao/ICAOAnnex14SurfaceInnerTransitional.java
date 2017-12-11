package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceInnerTransitional extends ICAOAnnex14Surface {

    private Double slope;

    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.INNER_TRANSITIONAL.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.INNER_TRANSITIONAL.description();
    }
}
