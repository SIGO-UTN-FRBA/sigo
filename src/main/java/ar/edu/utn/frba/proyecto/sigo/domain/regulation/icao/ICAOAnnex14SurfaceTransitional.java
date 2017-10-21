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
}
