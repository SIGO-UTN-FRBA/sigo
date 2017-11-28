package ar.edu.utn.frba.proyecto.sigo.persistence;

import ar.edu.utn.frba.proyecto.sigo.domain.airport.*;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.faa.RunwayClassificationFAA;
import ar.edu.utn.frba.proyecto.sigo.domain.airport.icao.RunwayClassificationICAOAnnex14;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.*;
import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocation;
import ar.edu.utn.frba.proyecto.sigo.domain.location.PoliticalLocationType;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.Region;
import ar.edu.utn.frba.proyecto.sigo.domain.analysis.State;
import ar.edu.utn.frba.proyecto.sigo.domain.object.*;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.OlsRule;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.faa.OlsRulesFAA;
import ar.edu.utn.frba.proyecto.sigo.domain.regulation.icao.OlsRuleICAOAnnex14;
import com.github.racc.tscg.TypesafeConfig;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class HibernateUtil {

    @Getter(AccessLevel.PUBLIC)
    private SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    @Inject
    public HibernateUtil(
        @TypesafeConfig("hibernate.connection.url") String url,
        @TypesafeConfig("hibernate.connection.username") String username,
        @TypesafeConfig("hibernate.connection.password") String password,
        @TypesafeConfig("hibernate.hikari.connectionTimeout") String connectionTimeout,
        @TypesafeConfig("hibernate.hikari.minimumIdle") String minimumIdle,
        @TypesafeConfig("hibernate.hikari.idleTimeout") String idleTimeout,
        @TypesafeConfig("hibernate.hikari.maximumPoolSize") String maximumPoolSize,
        @TypesafeConfig("hibernate.hbm2ddl.auto") String hb2ddl,
        @TypesafeConfig("hibernate.show_sql") Boolean showSQL
    ){
        StandardServiceRegistryBuilder registryBuilder =
                new StandardServiceRegistryBuilder();

        Map<String, Object> settings = new HashMap<>();
        settings.put(Environment.DRIVER, "org.postgresql.Driver");
        settings.put(Environment.DIALECT, "org.hibernate.spatial.dialect.postgis.PostgisDialect");
        settings.put(Environment.URL, url);
        settings.put(Environment.USER, username);
        settings.put(Environment.PASS, password);
        settings.put(Environment.HBM2DDL_AUTO, hb2ddl);
        settings.put(Environment.SHOW_SQL, showSQL);
        settings.put(Environment.FORMAT_SQL, showSQL);
        settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS,"org.hibernate.context.internal.ManagedSessionContext");

        // HikariCP settings

        // Maximum waiting time for a connection from the pool
        settings.put("hibernate.hikari.connectionTimeout", connectionTimeout);
        // Minimum number of ideal connections in the pool
        settings.put("hibernate.hikari.minimumIdle", minimumIdle);
        // Maximum number of actual connection in the pool
        settings.put("hibernate.hikari.maximumPoolSize", maximumPoolSize);
        // Maximum time that a connection is allowed to sit ideal in the pool
        settings.put("hibernate.hikari.idleTimeout", idleTimeout);

        registryBuilder.applySettings(settings);

        registry = registryBuilder.build();
        MetadataSources sources = new MetadataSources(registry)
                .addAnnotatedClass(Airport.class)
                .addAnnotatedClass(PlacedObject.class)
                .addAnnotatedClass(PlacedObjectBuilding.class)
                .addAnnotatedClass(PlacedObjectIndividual.class)
                .addAnnotatedClass(PlacedObjectOverheadWire.class)
                .addAnnotatedClass(PlacedObjectOwner.class)
                .addAnnotatedClass(PoliticalLocation.class)
                .addAnnotatedClass(PoliticalLocationType.class)
                .addAnnotatedClass(State.class)
                .addAnnotatedClass(Runway.class)
                .addAnnotatedClass(RunwayDirection.class)
                .addAnnotatedClass(RunwayClassification.class)
                .addAnnotatedClass(RunwayClassificationICAOAnnex14.class)
                .addAnnotatedClass(RunwayClassificationFAA.class)
                .addAnnotatedClass(RunwayApproachSection.class)
                .addAnnotatedClass(RunwayTakeoffSection.class)
                .addAnnotatedClass(OlsRule.class)
                .addAnnotatedClass(OlsRuleICAOAnnex14.class)
                .addAnnotatedClass(OlsRulesFAA.class)
                .addAnnotatedClass(Analysis.class)
                .addAnnotatedClass(AnalysisArea.class)
                .addAnnotatedClass(AnalysisAreaSurface.class)
                .addAnnotatedClass(AnalysisAreaObstacle.class)
                .addAnnotatedClass(AnalysisCase.class)
                .addAnnotatedClass(AnalysisException.class)
                .addAnnotatedClass(AnalysisExceptionRule.class)
                .addAnnotatedClass(AnalysisExceptionSurface.class)
                .addAnnotatedClass(AnalysisObject.class)
                .addAnnotatedClass(Region.class);

        Metadata metadata = sources.getMetadataBuilder().build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();

    }

}
