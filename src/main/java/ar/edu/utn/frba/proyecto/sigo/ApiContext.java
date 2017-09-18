package ar.edu.utn.frba.proyecto.sigo;

import ar.edu.utn.frba.proyecto.sigo.spark.Router;
import com.github.racc.tscg.TypesafeConfig;
import lombok.extern.slf4j.Slf4j;
import spark.Filter;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.Set;

import static spark.Spark.afterAfter;
import static spark.Spark.path;
import static spark.Spark.port;

@Slf4j
@Singleton
public class ApiContext {

    private final Integer port;
    private final String basePath;
    private String url;
    private final Set<Router> routers;

    @Inject
    public ApiContext(
            @TypesafeConfig("server.port") Integer port,
            @TypesafeConfig("app.context") String basePath,
            @TypesafeConfig("app.url") String url,
            Set<Router> routers
    ){

        this.port = port;
        this.basePath = basePath;
        this.url = url;
        this.routers = routers;
        this.url=url;
    }

    void init(){
        log.info("API endpoint is {}", url);

        port(port);

        Filter contentTypeFilter = (req, resp)-> resp.type("application/json");

        afterAfter(basePath + "/*", contentTypeFilter);
    }

    void configureRoutes() {

        routers.forEach(this::configureRoute);


    }

    private void configureRoute(Router router) {

        String fullPath = basePath + router.path();

        log.info("Define route {}", fullPath);

        path(fullPath, router.routes());
    }
}
