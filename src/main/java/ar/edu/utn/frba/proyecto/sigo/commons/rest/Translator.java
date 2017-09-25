package ar.edu.utn.frba.proyecto.sigo.commons.rest;

import ar.edu.utn.frba.proyecto.sigo.commons.persistence.HibernateUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class Translator<ENTITY, ENTITY_DTO> {

    protected Gson objectMapper;
    protected HibernateUtil hibernateUtil;
    protected Class<ENTITY_DTO> dtoClass;
    protected Class<ENTITY> domainClass;

    public abstract ENTITY_DTO getAsDTO(ENTITY domain);

    public ENTITY_DTO getAsDTO(JsonObject json){
        return objectMapper.fromJson(json, dtoClass);
    }

    public ENTITY_DTO getAsDTO(String string){
        return objectMapper.fromJson(string, dtoClass);
    }

    public abstract ENTITY getAsDomain(ENTITY_DTO dto);

    public ENTITY getAsDomain(JsonObject json){
        return objectMapper.fromJson(json, domainClass);
    };
}
