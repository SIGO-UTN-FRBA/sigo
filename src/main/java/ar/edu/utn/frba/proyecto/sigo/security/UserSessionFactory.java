package ar.edu.utn.frba.proyecto.sigo.security;

import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import com.github.racc.tscg.TypesafeConfig;
import com.google.common.cache.LoadingCache;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Singleton
public class UserSessionFactory {

    private LoadingCache<String, UserSession> usersCache;
    private String authorize;

    @Inject
    public UserSessionFactory(
            LoadingCache<String, UserSession> usersCache,
            @TypesafeConfig("auth0.audience2") String authorize
    ) {
        this.usersCache = usersCache;
        this.authorize = authorize;
    }

    public UserSession createUserSession(DecodedJWT jwt){

        try {
            return usersCache.get(
                jwt.getSubject(),
                () -> {
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
                });
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot create a user session for user " + jwt.getId());
        }
    }
}