package ar.edu.utn.frba.proyecto.sigo.domain.object;

public enum LightingTypes {
     R("Red"),
	 D("Medium intensity White Strobe & Red"),
	 H("High Intensity White Strobe & Red"),
	 M("Medium Intensity White Strobe"),
     S("High Intensity White Strobe"),
	 F("Flood"),
	 C("Dual Medium Catenary"),
 	 W("Synchronized Red Lighting"),
	 L("Lighted (Type Unknown)"),
	 N("None"),
	 U("Unknown");

    private final String type;

    LightingTypes(String type){
        this.type = type;
    }

    public String type(){
        return type;
    }

}
