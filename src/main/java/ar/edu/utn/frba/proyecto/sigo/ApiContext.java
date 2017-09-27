package ar.edu.utn.frba.proyecto.sigo;

import ar.edu.utn.frba.proyecto.sigo.dto.ExceptionDTO;
import ar.edu.utn.frba.proyecto.sigo.exception.*;
import ar.edu.utn.frba.proyecto.sigo.router.SigoRouter;
import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.github.racc.tscg.TypesafeConfig;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import spark.Filter;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.Set;

import static spark.Spark.*;

@Slf4j
@Singleton
public class ApiContext {

    private final Integer port;
    private final String basePath;
    private String url;
    private Gson jsonTransformer;
    private final Set<Router> routers;

    @Inject
    public ApiContext(
            @TypesafeConfig("server.port") Integer port,
            @TypesafeConfig("app.context") String basePath,
            @TypesafeConfig("app.url") String url,
            Gson jsonTransformer,
            Set<Router> routers
    ){

        this.port = port;
        this.basePath = basePath;
        this.url = url;
        this.jsonTransformer = jsonTransformer;
        this.routers = routers;
        this.url=url;
    }

    void init(){
        log.info("API endpoint is {}", url);

        port(port);

        this.configureExceptions();

        this.configureRoutes();

        this.configureContentTypes();

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
