package ar.edu.utn.frba.proyecto.sigo.domain.object;


public enum MarkIndicatorTypes {
	P("Orange or Orange and White Paint"),
	W("White Paint Only"),
	M("Marked"),
	F("Flag Marker"),
	S("Spherical Marker"),
	N("None"),
	U("Unknown");

    private final String type;

    MarkIndicatorTypes(String type){
        this.type = type;
    }

    public String type(){
        return type;
    }

    }

