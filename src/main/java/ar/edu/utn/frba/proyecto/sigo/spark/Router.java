package ar.edu.utn.frba.proyecto.sigo.spark;

import spark.RouteGroup;

public abstract class Router {

    public abstract RouteGroup routes();
    public abstract String path();
}
