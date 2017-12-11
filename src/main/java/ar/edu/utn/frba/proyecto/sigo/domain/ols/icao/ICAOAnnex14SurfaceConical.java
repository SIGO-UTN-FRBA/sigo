package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceConical extends ICAOAnnex14Surface {

    private Double slope;
    private Double height;
    private Double ratio;

    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.CONICAL.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.CONICAL.description();
    }
}
