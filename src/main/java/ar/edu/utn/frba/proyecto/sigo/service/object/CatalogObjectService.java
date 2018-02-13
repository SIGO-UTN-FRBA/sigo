package ar.edu.utn.frba.proyecto.sigo.service.object;

import ar.edu.utn.frba.proyecto.sigo.domain.object.ElevatedObjectTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.object.LightingTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.object.MarkIndicatorTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.object.TrackTypes;

import javax.inject.Singleton;

@Singleton
public class CatalogObjectService {

    public LightingTypes[] listLightingTypes() {
        return LightingTypes.values();
    }

    public MarkIndicatorTypes[] markIndicatorsTypes() {
        return MarkIndicatorTypes.values();
    }

    public ElevatedObjectTypes[] fetchObjectTypes() {
        return ElevatedObjectTypes.values();
    }

    public TrackTypes[] fetchTackSectionTypes() {
        return TrackTypes.values();
    }
}
