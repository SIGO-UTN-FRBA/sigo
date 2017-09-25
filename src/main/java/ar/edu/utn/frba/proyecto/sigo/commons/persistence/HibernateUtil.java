package ar.edu.utn.frba.proyecto.sigo.commons.persistence;

import ar.edu.utn.frba.proyecto.sigo.domain.Airport;
import ar.edu.utn.frba.proyecto.sigo.domain.Runway;
import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.github.racc.tscg.TypesafeConfig;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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
        settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);

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
                .addAnnotatedClass(Runway.class);

        Metadata metadata = sources.getMetadataBuilder().build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();

    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public <T>T doInTransaction(Function<Session, T> function){

        T result;

        try(Session session = this.getSessionFactory().openSession()){

            try{
                session.getTransaction().begin();

                result = function.apply(session);

                session.getTransaction().commit();

            } catch (Exception e){

                if ( session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK ) {
                    session.getTransaction().rollback();
                }

                throw new SigoException(e);
            }
        }

        return result;
    }

    public void doInTransaction(Consumer<Session> function){

        try(Session session = this.getSessionFactory().openSession()){

            try{
                session.getTransaction().begin();

                function.accept(session);

                session.getTransaction().commit();

            } catch (Exception e){

                if ( session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK ) {
                    session.getTransaction().rollback();
                }

                throw new SigoException(e);
            }
        }
    }
}
