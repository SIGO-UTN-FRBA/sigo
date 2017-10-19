package ar.edu.utn.frba.proyecto.sigo.service.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulation;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.AirportDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.Translator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AirportTranslator extends Translator<Airport, AirportDTO> {

    private RegionService regionService;
    private RegulationService regulationService;

    @Inject
    public AirportTranslator(
            Gson gson,
            RegionService regionService,
            RegulationService regulationService
    ){
        this.regionService = regionService;
        this.objectMapper = gson;
        this.dtoClass = AirportDTO.class;
        this.domainClass = Airport.class;
        this.regulationService = regulationService;
    }

    @Override
    public AirportDTO getAsDTO(Airport domain) {
        return AirportDTO.builder()
                .id(domain.getId())
                .codeFIR(domain.getCodeFIR())
                .codeIATA(domain.getCodeIATA())
                .nameFIR(domain.getNameFIR())
                .regionId(domain.getRegion().getId())
                .regulationId(domain.getRegulation().getId())
                .build();
    }

    @Override
    public Airport getAsDomain(AirportDTO dto) {

        Airport.AirportBuilder builder = Airport.builder();

        // basic properties
        builder
            .id(dto.getId())
            .codeFIR(dto.getCodeFIR())
            .codeIATA(dto.getCodeIATA())
            .nameFIR(dto.getNameFIR());

        // relation: region
        Region region = this.regionService.get(dto.getRegionId());
        if(!Optional.ofNullable(region).isPresent())
            throw new InvalidParameterException("region_id == " + dto.getRegionId());
        builder.region(region);

        // relation: regulation
        Regulation regulation = this.regulationService.get(dto.getRegulationId());
        if(!Optional.ofNullable(regulation).isPresent())
            throw new InvalidParameterException("regulation_id == " + dto.getRegulationId());
        builder.regulation(regulation);


        return builder.build();
    }
}
