package ar.edu.utn.frba.proyecto.sigo.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class Translator<ENTITY, ENTITY_DTO> {

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
