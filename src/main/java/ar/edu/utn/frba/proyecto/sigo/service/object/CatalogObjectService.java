package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.LightingTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.object.MarkIndicatorTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectTypes;

public class CatalogObjectService {

    public LightingTypes[] listLightingTypes() {
        return LightingTypes.values();
    }

    public MarkIndicatorTypes[] markIndicatorsTypes() {
        return MarkIndicatorTypes.values();
    }

    public PlacedObjectTypes[] fetchObjectTypes() {
        return PlacedObjectTypes.values();
    }
}
