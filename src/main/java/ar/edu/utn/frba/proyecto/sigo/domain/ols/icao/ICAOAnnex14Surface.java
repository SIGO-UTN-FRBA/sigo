package ar.edu.utn.frba.proyecto.sigo.domain.ols.icao;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.ols.ObstacleLimitationSurface;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public abstract class ICAOAnnex14Surface
        extends SigoDomain
        implements ObstacleLimitationSurface {

    public abstract ICAOAnnex14Surfaces getEnum();

    private ICAOAnnex14RunwayClassifications classification;
    private ICAOAnnex14RunwayCategories category;
    private ICAOAnnex14RunwayCodeNumbers code;

}
