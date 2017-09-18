package ar.edu.utn.frba.proyecto.sigo.spark;

import spark.RouteGroup;

public interface Router {

    RouteGroup routes();
    String path();
}
