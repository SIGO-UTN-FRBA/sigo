package ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class ICAOAnnex14Surface {

    private ICAOAnnex14RunwayClassifications classification;
    private ICAOAnnex14RunwayCategories category;
    private ICAOAnnex14RunwayCodeNumbers code;
}
