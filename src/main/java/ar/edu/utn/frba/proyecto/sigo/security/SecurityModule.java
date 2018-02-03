package ar.edu.utn.frba.proyecto.sigo.security;

import ar.edu.utn.frba.proyecto.sigo.exception.SigoException;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.github.racc.tscg.TypesafeConfig;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class SecurityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserSessionFactory.class);
    }

    @Provides
    LoadingCache<String, UserSession> provideUserLoadingCache(){
        return UserSessionGuavaCacheUtil.getLoadingCache();
    }

    @Provides
    RSAKeyProvider provideRSAKeyProvider(@TypesafeConfig("auth0.jwksUri") String jwksUri){

        JwkProvider jwkProvider = new JwkProviderBuilder(jwksUri).build();

        return new RSAKeyProvider() {
            @Override
            public RSAPublicKey getPublicKeyById(String kid) {

                try {
                    return (RSAPublicKey) jwkProvider.get(kid).getPublicKey();

                } catch (JwkException e) {
                    throw new SigoException(e);
                }

            }

            @Override
            public RSAPrivateKey getPrivateKey() {
                return null;
            }

            @Override
            public String getPrivateKeyId() {
                return null;
            }
        };
    }
}
