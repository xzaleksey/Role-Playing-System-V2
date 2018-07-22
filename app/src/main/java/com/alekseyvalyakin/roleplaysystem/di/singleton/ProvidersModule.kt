package com.alekseyvalyakin.roleplaysystem.di.singleton

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Base app module
 */
@Module
class ProvidersModule() {

    @Provides
    @Singleton
    fun provideAuthProvider(authProviderImpl: AuthProviderImpl): AuthProvider {
        return authProviderImpl
    }

}
