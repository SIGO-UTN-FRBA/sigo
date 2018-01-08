package ar.edu.utn.frba.proyecto.sigo.translator.object;

import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.domain.object.LightingTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.object.MarkIndicatorTypes;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObject;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectBuilding;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectIndividual;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOverheadWire;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectOwner;
import ar.edu.utn.frba.proyecto.sigo.domain.object.PlacedObjectTypes;
import ar.edu.utn.frba.proyecto.sigo.dto.object.PlacedObjectDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.object.PlacedObjectOwnerService;
import ar.edu.utn.frba.proyecto.sigo.translator.Translator;
import ar.edu.utn.frba.proyecto.sigo.service.location.PoliticalLocationService;
import ar.edu.utn.frba.proyecto.sigo.service.location.RegionService;
import com.google.gson.Gson;

import javax.inject.Inject;
import java.util.Optional;

public class PlacedObjectTranslator extends Translator<PlacedObject, PlacedObjectDTO>{

    private PlacedObjectOwnerService ownerService;
    private PoliticalLocationService locationService;
    private RegionService regionService;

    @Inject
    public PlacedObjectTranslator(
            Gson gson,
            PlacedObjectOwnerService ownerService,
            PoliticalLocationService locationService,
            RegionService regionService
    ){
        this.locationService = locationService;
        this.regionService = regionService;
        this.objectMapper = gson;
        this.ownerService = ownerService;
    };

    @Override
    public PlacedObjectDTO getAsDTO(PlacedObject domain) {
        return PlacedObjectDTO.builder()
                    .id(domain.getId())
                    .heightAgl(domain.getHeightAgl())
                    .heightAmls(domain.getHeightAmls())
                    .lightingId(domain.getLighting().ordinal())
                    .locationId(domain.getPoliticalLocation().getId())
                    .markIndicatorId(domain.getMarkIndicator().ordinal())
                    .name(domain.getName())
                    .ownerId(domain.getOwner().getId())
                    .typeId(domain.getType().ordinal())
                    .subtype(domain.getSubtype())
                    .verified(domain.getVerified())
                    .temporary(domain.getTemporary())
                    .build();
    }

    @Override
    public PlacedObject getAsDomain(PlacedObjectDTO dto) {

        switch (PlacedObjectTypes.values()[dto.getTypeId()]){

            case BUILDING:
                return getAsBuildingDomain(dto);
            case INDIVIDUAL:
                return getAsIndividualDomain(dto);
            case OVERHEAD_WIRED:
                return getAsWiredDomain(dto);
        }

        throw new InvalidParameterException("Invalid object type");
    }

    private PlacedObjectOverheadWire getAsWiredDomain(PlacedObjectDTO dto) {

        PlacedObjectOverheadWire.PlacedObjectOverheadWireBuilder builder = PlacedObjectOverheadWire.builder();

        // basic properties
        builder
                .id(dto.getId())
                .heightAgl(dto.getHeightAgl())
                .heightAmls(dto.getHeightAmls())
                .lighting(LightingTypes.values()[dto.getLightingId()])
                .markIndicator(MarkIndicatorTypes.values()[dto.getMarkIndicatorId()])
                .name(dto.getName())
                .type(PlacedObjectTypes.values()[dto.getTypeId()])
                .subtype(dto.getSubtype())
                .verified(dto.getVerified())
                .temporary(dto.getTemporary());

        // relation: owner
        PlacedObjectOwner owner = Optional
                .ofNullable(this.ownerService.get(dto.getOwnerId()))
                .orElseThrow(()-> new InvalidParameterException("ownerId == " + dto.getOwnerId()));

        builder.owner(owner);


        // relation: location
        PoliticalLocation location = Optional
                .ofNullable(this.locationService.get(dto.getLocationId()))
                .orElseThrow(()-> new InvalidParameterException("locationId == " + dto.getLocationId()));

        builder.politicalLocation(location);

        return builder.build();
    }

    private PlacedObject getAsIndividualDomain(PlacedObjectDTO dto) {

        PlacedObjectIndividual.PlacedObjectIndividualBuilder builder = PlacedObjectIndividual.builder();

        // basic properties
        builder
                .id(dto.getId())
                .heightAgl(dto.getHeightAgl())
                .heightAmls(dto.getHeightAmls())
                .lighting(LightingTypes.values()[dto.getLightingId()])
                .markIndicator(MarkIndicatorTypes.values()[dto.getMarkIndicatorId()])
                .name(dto.getName())
                .type(PlacedObjectTypes.values()[dto.getTypeId()])
                .subtype(dto.getSubtype())
                .verified(dto.getVerified())
                .temporary(dto.getTemporary());

        // relation: owner
        PlacedObjectOwner owner = Optional
                .ofNullable(this.ownerService.get(dto.getOwnerId()))
                .orElseThrow(()-> new InvalidParameterException("ownerId == " + dto.getOwnerId()));

        builder.owner(owner);


        // relation: location
        PoliticalLocation location = Optional
                .ofNullable(this.locationService.get(dto.getLocationId()))
                .orElseThrow(()-> new InvalidParameterException("locationId == " + dto.getLocationId()));

        builder.politicalLocation(location);

        return builder.build();
    }

    private PlacedObjectBuilding getAsBuildingDomain(PlacedObjectDTO dto) {

        PlacedObjectBuilding.PlacedObjectBuildingBuilder builder = PlacedObjectBuilding.builder();

        // basic properties
        builder
                .id(dto.getId())
                .heightAgl(dto.getHeightAgl())
                .heightAmls(dto.getHeightAmls())
                .lighting(LightingTypes.values()[dto.getLightingId()])
                .markIndicator(MarkIndicatorTypes.values()[dto.getMarkIndicatorId()])
                .name(dto.getName())
                .type(PlacedObjectTypes.values()[dto.getTypeId()])
                .subtype(dto.getSubtype())
                .verified(dto.getVerified())
                .temporary(dto.getTemporary());

        // relation: owner
        PlacedObjectOwner owner = Optional
                .ofNullable(this.ownerService.get(dto.getOwnerId()))
                .orElseThrow(()-> new InvalidParameterException("ownerId == " + dto.getOwnerId()));

        builder.owner(owner);


        // relation: location
        PoliticalLocation location = Optional
                .ofNullable(this.locationService.get(dto.getLocationId()))
                .orElseThrow(()-> new InvalidParameterException("locationId == " + dto.getLocationId()));

        builder.politicalLocation(location);

        return builder.build();
    }
}
