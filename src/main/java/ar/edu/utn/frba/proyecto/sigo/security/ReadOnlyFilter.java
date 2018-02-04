package ar.edu.utn.frba.proyecto.sigo.security;

import ar.edu.utn.frba.proyecto.sigo.exception.UnauthorizedRequestException;
import org.eclipse.jetty.http.HttpMethod;
import spark.Filter;
import spark.Request;
import spark.Response;

import javax.inject.Singleton;

@Singleton
public class ReadOnlyFilter implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {

        if(request.requestMethod().equals(HttpMethod.OPTIONS.asString()))
            return;

        UserSession session = request.attribute("current-session");

        if(session.getRole().equals(SigoRoles.READONLY) && !request.requestMethod().equals(HttpMethod.GET.asString()))
            throw new UnauthorizedRequestException("You are only authorized to read resources.");
    }
}
