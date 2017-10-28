package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayClassification;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.RunwayDirection;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.faa.RunwayClassificationFAA;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.icao.RunwayClassificationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCategories;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayClassifications;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeLetters;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.ICAOAnnex14RunwayCodeNumbers;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.RunwayClassificationDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RunwayClassificationTranslator extends Translator<RunwayClassification, RunwayClassificationDTO> {

    private RunwayDirectionService directionService;

    @Inject
    public RunwayClassificationTranslator(
            Gson objectMapper,
            RunwayDirectionService directionService
    ){
        this.directionService = directionService;
        this.objectMapper = objectMapper;
        this.dtoClass = RunwayClassificationDTO.class;
        this.domainClass = RunwayClassification.class;
    }

    @Override
    public RunwayClassificationDTO getAsDTO(RunwayClassification domain) {

        return domain.accept(new ToDTO());
    }

    @Override
    public RunwayClassification getAsDomain(RunwayClassificationDTO dto) {

        RunwayClassification classification = null;

        switch (dto.getType()){
            case "RunwayClassificationICAOAnnex14":
                classification = this.getAsICAODomain(dto);
                break;
            case "RunwayClassificationFAA":
                classification = this.getAsFAADomain();
                break;
        }

        return classification;
    }

    private RunwayClassificationFAA getAsFAADomain() {
        return null;
    }

    private RunwayClassificationICAOAnnex14 getAsICAODomain(RunwayClassificationDTO dto) {

        RunwayClassificationICAOAnnex14.RunwayClassificationICAOAnnex14Builder builder = RunwayClassificationICAOAnnex14.builder();

        // base properties

        builder
                .id(dto.getId())
                .runwayClassification(ICAOAnnex14RunwayClassifications.values()[dto.getRunwayClassification()])
                .runwayCategory(ICAOAnnex14RunwayCategories.values()[dto.getRunwayCategory()])
                .runwayTypeLetter(ICAOAnnex14RunwayCodeLetters.values()[dto.getRunwayTypeLetter()])
                .runwayTypeNumber(ICAOAnnex14RunwayCodeNumbers.values()[dto.getRunwayTypeNumber()]);

        // relation: runway direction
        RunwayDirection direction = Optional.ofNullable(directionService.get(dto.getDirectionId()))
                .orElseThrow( () -> new InvalidParameterException("directionId == " + dto.getDirectionId()));
        builder.runwayDirection(direction);

        return builder.build();
    }

    private class ToDTO implements RunwayClassificationVisitor<RunwayClassificationDTO>{

        @Override
        public RunwayClassificationDTO visitRunwayClassificationICAOAnnex14(RunwayClassificationICAOAnnex14 domain) {

            RunwayClassificationDTO.RunwayClassificationDTOBuilder builder = RunwayClassificationDTO.builder();

            Optional.ofNullable(domain.getRunwayClassification()).ifPresent(x -> builder.runwayClassification(x.ordinal()));

            Optional.ofNullable(domain.getRunwayCategory()).ifPresent(x-> builder.runwayCategory(x.ordinal()));

            Optional.ofNullable(domain.getRunwayTypeLetter()).ifPresent(x -> builder.runwayTypeLetter(x.ordinal()));

            Optional.ofNullable(domain.getRunwayTypeNumber()).ifPresent(x -> builder.runwayTypeNumber(x.ordinal()));

            return builder
                    .id(domain.getId())
                    .type(RunwayClassificationICAOAnnex14.class.getSimpleName())
                    .directionId(domain.getRunwayDirection().getId())
                    .build();
        }

        @Override
        public RunwayClassificationDTO visitRunwayClassificationFAA(RunwayClassificationFAA domain) {

            RunwayClassificationDTO.RunwayClassificationDTOBuilder builder = RunwayClassificationDTO.builder();

            Optional.ofNullable(domain.getRunwayClassification()).ifPresent(x -> builder.runwayClassification(x.ordinal()));

            Optional.ofNullable(domain.getRunwayCategory()).ifPresent(x-> builder.runwayCategory(x.ordinal()));

            Optional.ofNullable(domain.getAircraftApproachCategory()).ifPresent(x -> builder.aircraftApproachCategory(x.ordinal()));

            Optional.ofNullable(domain.getAircraftClassification()).ifPresent(x -> builder.aircraftClassification(x.ordinal()));

            Optional.ofNullable(domain.getRunwayTypeLetter()).ifPresent(x -> builder.runwayTypeLetter(x.ordinal()));

            return builder
                    .id(domain.getId())
                    .type(RunwayClassificationFAA.class.getSimpleName())
                    .directionId(domain.getRunwayDirection().getId())
                    .build();
        }
    }
}
