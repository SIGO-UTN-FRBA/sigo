package ar.edu.utn.frba.proyecto.sigo.domain.regulation;

public enum Regulations {

    ICAO_ANNEX_14("ICAO Annex 14"),                 // 0
    EASA("EASA"),                                   // 1
    BMVBW("BMVBW (Germany)"),                       // 2
    TRANSPORT_CANADA_5TH("TP 312 4th (Canada)"),    // 3
    TRANSPORT_CANADA_4TH("TP 312 5th (Canada)"),    // 4
    FAA_CFR_PART_77("FAA - CFR Part 77"),           // 5
    FAA_AC150_5300_13A("FAA - AC150-5300-13A"),     // 6
    FAA_AC150_5300_18B("FAA - AC150-5300-18B");     // 7

    private String description;

    Regulations(String s) {
        this.description = s;
    }

    public String description() {
        return description;
    }
}
