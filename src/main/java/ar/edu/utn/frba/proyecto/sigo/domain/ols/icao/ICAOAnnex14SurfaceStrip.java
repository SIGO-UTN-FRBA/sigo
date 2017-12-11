package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ICAOAnnex14SurfaceStrip extends ICAOAnnex14Surface {
    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.STRIP.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.STRIP.description();
    }

    private Double length;
    private Double width;
}
