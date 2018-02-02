package ar.edu.utn.frba.proyecto.sigo.security;

import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import com.github.racc.tscg.TypesafeConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class UserSessionFactory {

    private String authorize;

    @Inject
    public UserSessionFactory(@TypesafeConfig("auth0.audience2") String authorize) {
        this.authorize = authorize;
    }

    public UserSession createUserSession(DecodedJWT jwt){

        try (CloseableHttpClient httpClient = HttpClients.createDefault()){

            HttpGet request = new HttpGet(authorize);

            request.setHeader("Authorization", "Bearer " + jwt.getToken());

            CloseableHttpResponse response = httpClient.execute(request);

            Payload payload = new JWTParser().parsePayload(EntityUtils.toString(response.getEntity()));

            return new UserSession(payload);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot create a user session for user " + jwt.getId());
        }
    }
}