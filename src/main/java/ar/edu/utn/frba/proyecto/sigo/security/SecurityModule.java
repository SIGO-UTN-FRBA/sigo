package ar.edu.utn.frba.proyecto.sigo.security;

import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class SecurityModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    LoadingCache<String, UserSession> provideUserLoadingCache(){
        return UserSessionGuavaCacheUtil.getLoadingCache();
    }
}
