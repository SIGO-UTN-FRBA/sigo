package ar.edu.utn.frba.proyecto.sigo.domain.regulation;

import ar.edu.utn.frba.proyecto.sigo.domain.SigoDomain;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.bmvbw.RegulationBMVBW;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.easa.RegulationEASA;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.RegulationFAA13A;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.RegulationFAA18B;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.RegulationFAAPart77;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.RegulationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.tcanada.RegulationTpCanada312_4;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.tcanada.RegulationTpCanada312_5;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public abstract class Regulation extends SigoDomain<Long> {
    private Long id;
    private String authority;
    private String name;
    private String description;

    public static Regulation of(Regulations regulation){
        switch (regulation) {
            case ICAO_ANNEX_14:
                return new RegulationICAOAnnex14();
            case EASA:
                return new RegulationEASA();
            case BMVBW:
                return new RegulationBMVBW();
            case TRANSPORT_CANADA_4TH:
                return new RegulationTpCanada312_4();
            case TRANSPORT_CANADA_5TH:
                return new RegulationTpCanada312_5();
            case FAA_CFR_PART_77:
                return new RegulationFAAPart77();
            case FAA_AC150_5300_13A:
                return new RegulationFAA13A();
            case FAA_AC150_5300_18B:
                return new RegulationFAA18B();
        }

        throw new SigoException("Regulation does not exists");
    }
}