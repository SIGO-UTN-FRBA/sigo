package ar.edu.utn.frba.proyecto.sigo.translator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public abstract class SigoTranslator<ENTITY, ENTITY_DTO> {

    protected Gson objectMapper;
    protected Class<ENTITY_DTO> dtoClass;
    protected Class<ENTITY> domainClass;

    public abstract ENTITY_DTO getAsDTO(ENTITY domain);

    public abstract ENTITY getAsDomain(ENTITY_DTO dto);

    public ENTITY_DTO getAsDTO(JsonObject json){
        return objectMapper.fromJson(json, dtoClass);
    }

    public ENTITY_DTO getAsDTO(String string){
        return objectMapper.fromJson(string, dtoClass);
    }

    public ENTITY getAsDomain(JsonObject json){
        return objectMapper.fromJson(json, domainClass);
    };
}
