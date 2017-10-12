package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.LightingTypes;

public class CatalogObjectService {

    public LightingTypes[] listLightingTypes() {
        return LightingTypes.values();
    }
}
