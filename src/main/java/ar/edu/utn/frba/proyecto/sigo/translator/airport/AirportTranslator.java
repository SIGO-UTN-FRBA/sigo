package ar.edu.utn.frba.proyecto.sigo.translator.airport;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.Regulations;
import ar.edu.utn.frba.proyecto.sigo.dto.airport.AirportDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.InvalidParameterException;
import ar.edu.utn.frba.proyecto.sigo.service.airport.RegionService;
import ar.edu.utn.frba.proyecto.sigo.translator.SigoTranslator;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AirportTranslator extends SigoTranslator<Airport, AirportDTO> {

    private RegionService regionService;

    @Inject
    public AirportTranslator(
            Gson gson,
            RegionService regionService
    ){
        super(gson, AirportDTO.class, Airport.class);

        this.regionService = regionService;
    }

    @Override
    public AirportDTO getAsDTO(Airport domain) {
        return AirportDTO.builder()
                .id(domain.getId())
                .codeFIR(domain.getCodeFIR())
                .codeIATA(domain.getCodeIATA())
                .codeLocal(domain.getCodeLocal())
                .nameFIR(domain.getNameFIR())
                .regionId(domain.getRegion().getId())
                .regulationId(domain.getRegulation().ordinal())
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
            .codeLocal(dto.getCodeLocal())
            .nameFIR(dto.getNameFIR())
            .regulation(Regulations.values()[dto.getRegulationId()]);

        // relation: region
        Region region = this.regionService.get(dto.getRegionId());
        if(!Optional.ofNullable(region).isPresent())
            throw new InvalidParameterException("region_id == " + dto.getRegionId());
        builder.region(region);

        return builder.build();
    }
}
