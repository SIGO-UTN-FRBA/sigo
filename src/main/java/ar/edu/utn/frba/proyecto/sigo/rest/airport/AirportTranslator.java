package ar.edu.utn.frba.proyecto.sigo.rest.airport;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import ar.edu.utn.frba.proyecto.sigo.commons.rest.Translator;
import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.dto.AirportDTO;
import com.google.gson.Gson;

import javax.inject.Inject;

public class AirportTranslator extends Translator<Airport, AirportDTO> {

    @Inject
    public AirportTranslator(
            HibernateUtil hibernateUtil,
            Gson gson
    ){
        this.hibernateUtil = hibernateUtil;
        this.objectMapper = gson;
        this.dtoClass = AirportDTO.class;
        this.domainClass = Airport.class;
    }

    @Override
    public AirportDTO getAsDTO(Airport domain) {
        return AirportDTO.builder()
                .id(domain.getId())
                .codeFIR(domain.getCodeFIR())
                .codeIATA(domain.getCodeIATA())
                .nameFIR(domain.getNameFIR())
                .build();
    }

    @Override
    public Airport getAsDomain(AirportDTO dto) {
        return Airport.builder()
                .id(dto.getId())
                .codeFIR(dto.getCodeFIR())
                .codeIATA(dto.getCodeIATA())
                .nameFIR(dto.getNameFIR())
                .build();
    }
}
