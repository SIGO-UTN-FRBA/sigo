package ar.edu.utn.frba.proyecto.sigo.security;

import ar.edu.utn.frba.proyecto.sigo.domain.user.SigoUser;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Singleton
public class UserSessionFactory {

    private LoadingCache<String, UserSession> usersCache;
    private SessionFactory sessionFactory;
    private String authorize;

    @Inject
    public UserSessionFactory(
            LoadingCache<String, UserSession> usersCache,
            SessionFactory sessionFactory,
            @TypesafeConfig("auth0.audience2") String authorize
    ) {
        this.usersCache = usersCache;
        this.sessionFactory = sessionFactory;
        this.authorize = authorize;
    }

    public UserSession createUserSession(DecodedJWT jwt){

        try {
            return usersCache.get(
                jwt.getSubject(),
                () -> {

                    UserSession userSession;

                    try (CloseableHttpClient httpClient = HttpClients.createDefault()){

                        HttpGet request = new HttpGet(authorize);

                        request.setHeader("Authorization", "Bearer " + jwt.getToken());

                        CloseableHttpResponse response = httpClient.execute(request);

                        Payload payload = new JWTParser().parsePayload(EntityUtils.toString(response.getEntity()));

                        userSession = new UserSession(payload);

                    } catch (IOException e) {
                        throw new RuntimeException("Fail to create a user session " + jwt.getId());
                    }

                    persistUser(userSession.getUser());

                    return userSession;
                });
        } catch (ExecutionException e) {
            throw new RuntimeException("Fail to retrieve a user session " + jwt.getId());
        }
    }

    private SigoUser persistUser(SigoUser user) {
        try(Session session = sessionFactory.openSession()){

            Transaction transaction = session.beginTransaction();

            session.saveOrUpdate(user);

            transaction.commit();

        } catch (Exception e){
            throw new RuntimeException("Fail to persist a user " + user.getId());
        }

        return user;
    }

    public UserSession createFakeUserSession(DecodedJWT jwt) {

        try {
            return usersCache.get(jwt.getSubject(), () -> {

                UserSession userSession = new UserSession(jwt);

                persistUser(userSession.getUser());

                return userSession;
            });
        } catch (ExecutionException e) {
            throw new RuntimeException("Fail to retrieve a user session " + jwt.getId());
        }
    }
}