package ar.edu.utn.frba.proyecto.sigo.commons.persistence;

import ar.edu.utn.frba.proyecto.sigo.airport.Airport;
import com.github.racc.tscg.TypesafeConfig;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.mapping.MetadataSource;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.metamodel.EntityType;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class HibernateUtil {

    @Getter
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
                .addAnnotatedClass(Airport.class);
        Metadata metadata = sources.getMetadataBuilder().build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();

    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
