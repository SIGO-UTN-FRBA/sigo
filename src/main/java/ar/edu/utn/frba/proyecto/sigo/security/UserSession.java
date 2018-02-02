package ar.edu.utn.frba.proyecto.sigo.security;

import com.auth0.jwt.interfaces.Payload;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserSession {

    private Payload payload;

    public String getUserId(){
        return payload.getId();
    }
}
