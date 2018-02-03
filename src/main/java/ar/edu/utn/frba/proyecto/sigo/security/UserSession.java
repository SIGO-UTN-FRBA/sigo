package ar.edu.utn.frba.proyecto.sigo.security;

import ar.edu.utn.frba.proyecto.sigo.domain.user.SigoUser;
import com.auth0.jwt.interfaces.Payload;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Optional;

@NoArgsConstructor
@Data
public class UserSession {

    private Payload payload;

    private SigoUser user;

    public UserSession(Payload payload) {

        this.payload = payload;

        this.user = SigoUser.builder()
                .id(payload.getSubject())
                .name(payload.getClaim("name").asString())
                .nickname(payload.getClaim("nickname").asString())
                .email(payload.getClaim("email").asString())
                .build();
    }

    public Optional<HashMap<String,Object>> getAppMetadata(){
        return Optional.ofNullable(this.payload.getClaim("http://localhost:8080/sigo/api/app_metadata").as(HashMap.class));
    }

    public Optional<SigoRoles> getRole(){

        return this.getAppMetadata()
                .map(metadata -> metadata.getOrDefault("role", null))
                .map(value -> SigoRoles.valueOf((String) value));

    }
}
