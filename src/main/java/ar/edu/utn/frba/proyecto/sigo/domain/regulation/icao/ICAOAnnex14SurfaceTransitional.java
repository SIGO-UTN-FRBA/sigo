package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Data
@Builder
public class ICAOAnnex14SurfaceTransitional extends ICAOAnnex14Surface {

    private Double slope;

    @Override
    public Long getId() {
        return (long)ICAOAnnex14Surfaces.TRANSITIONAL.ordinal();
    }

    @Override
    public String getName() {
        return ICAOAnnex14Surfaces.TRANSITIONAL.description();
    }
}
