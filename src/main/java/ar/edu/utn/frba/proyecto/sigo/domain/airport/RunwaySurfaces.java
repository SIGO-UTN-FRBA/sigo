package ar.edu.utn.frba.proyecto.sigo.domain.airport;

public enum RunwaySurfaces {
    ASP("Asphalt"),                                     //0
    BIT("Bitumenous asphalt or tarmac"),                //1
    BRI("Bricks (no longer in use)"),                   //2
    CLA("Clay"),                                        //3
    COM("Composite"),                                   //4
    CON("Concrete"),                                    //5
    COP("Composite"),                                   //6
    COR("Coral (fine crushed coral reef structures)"),  //7
    GRE("Graded or rolled earth"),                      //8
    GRS("Grass or earth not graded or rolled"),         //9
    GVL("Gravel"),                                      //10
    ICE("Ice"),                                         //11
    LAT("Laterite"),                                    //12
    MAC("Macadam"),                                     //13
    PC("Partially concrete"),                           //14
    PS("Permanent surface"),                            //15
    PSP("Marston Matting (derived from pierced/perforated steel planking)"),
    SAN("Sand"),                                        //17
    SMT("Sommerfeld Tracking"),                         //18
    SNO("Snow"),                                        //19
    U("Unknown surface");                               //20


    private String description;

    RunwaySurfaces(String s) {
        this.description = s;
    }


    public String description() {
        return description;
    }
}
