package ar.edu.utn.frba.proyecto.sigo;

import ar.edu.utn.frba.proyecto.sigo.dto.common.ExceptionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.*;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.racc.tscg.TypesafeConfig;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import spark.Filter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import static spark.Spark.*;

@Slf4j
@Singleton
public class ApiContext {

    private final Integer port;
    private final String basePath;
    private String url;
    private String secret;
    private String audience;
    private String issuer;
    private Gson jsonTransformer;
    private final Set<Router> routers;

    @Inject
    public ApiContext(
            @TypesafeConfig("server.port") Integer port,
            @TypesafeConfig("app.context") String basePath,
            @TypesafeConfig("app.url") String url,
            @TypesafeConfig("auth0.clientSecret") String secret,
            @TypesafeConfig("auth0.audience") String audience,
            @TypesafeConfig("auth0.issuer") String issuer,
            Gson jsonTransformer,
            Set<Router> routers
    ){

        this.port = port;
        this.basePath = basePath;
        this.url = url;
        this.secret = secret;
        this.audience = audience;
        this.issuer = issuer;
        this.jsonTransformer = jsonTransformer;
        this.routers = routers;
        this.url=url;
    }

    void init(){
        log.info("API endpoint is {}", url);

        port(port);

        this.configureCors();

        this.configureAuth();

        this.configureExceptions();

        this.configureRoutes();

        this.configureContentTypes();

    }

    private void configureAuth() {

        Algorithm algorithm = null;

        try {
            algorithm = Algorithm.HMAC256(secret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .build();

        before(basePath + "/*", (request, response) -> {

            if(request.requestMethod().equals(HttpMethod.OPTIONS.asString()))
                    return;

            try {

                verifier.verify(request.headers("authorization"));

            } catch (JWTVerificationException e){
                //Invalid signature/claims
                throw new UnauthorizedRequestException(e);
            }
        });

    }

    private void configureCors() {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            response.header("Access-Control-Allow-Credentials", "true");
        });
    }

    private void configureContentTypes() {
        Filter contentTypeFilter = (req, resp)-> resp.type("application/json");

        afterAfter(basePath + "/*", contentTypeFilter);
    }

    private void configureExceptions() {

        exception(SigoException.class, (e, request, response) ->{
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            response.body(jsonTransformer.toJson(
                    new ExceptionDTO(
                            SigoException.class.getSimpleName(),
                            e.getMessage()
                    )
                )
            );
        });

        exception(BusinessConstrainException.class, (e, request, response) -> {
            response.status(HttpStatus.BAD_REQUEST_400);
            response.body(jsonTransformer.toJson(
                    new ExceptionDTO(
                            BusinessConstrainException.class.getSimpleName(),
                            e.getMessage()
                    )
                )
            );
        });

        exception(MissingParameterException.class, (e, request, response) ->{
            response.status(HttpStatus.BAD_REQUEST_400);
            response.body(jsonTransformer.toJson(
                    new ExceptionDTO(
                            MissingParameterException.class.getSimpleName(),
                            e.getMessage()
                    )
                )
            );
        });

        exception(ResourceNotFoundException.class, (e, request, response) ->{
            response.status(HttpStatus.NOT_FOUND_404);
            response.body(jsonTransformer.toJson(
                    new ExceptionDTO(
                            ResourceNotFoundException.class.getSimpleName(),
                            e.getMessage()
                    )
                )
            );
        });

        exception(InvalidParameterException.class, (e, request, response) ->{
            response.status(HttpStatus.BAD_REQUEST_400);
            response.body(jsonTransformer.toJson(
                    new ExceptionDTO(
                            InvalidParameterException.class.getSimpleName(),
                            e.getMessage()
                    )
                )
            );
        });

        exception(UnauthorizedRequestException.class, (e, request, response) ->{
            response.status(HttpStatus.UNAUTHORIZED_401);
            response.body(jsonTransformer.toJson(
                    new ExceptionDTO(
                            UnauthorizedRequestException.class.getSimpleName(),
                            e.getMessage()
                    )
                    )
            );
        });
    }

    private void configureRoutes() {
        routers.forEach(this::configureRoute);
    }

    private void configureRoute(Router router) {

        String fullPath = basePath + router.path();

        log.info("Define route {}", fullPath);

        path(fullPath, router.routes());
    }
}
