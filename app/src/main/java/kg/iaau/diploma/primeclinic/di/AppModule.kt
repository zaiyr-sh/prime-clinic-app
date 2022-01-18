package kg.iaau.diploma.primeclinic.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.iaau.diploma.local_storage.StoragePreferences
import kg.iaau.diploma.network.interceptors.ApiAuth
import kg.iaau.diploma.primeclinic.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesStoragePreferences(@ApplicationContext context: Context) = StoragePreferences(context)

    @Singleton
    @Provides
    fun providesAuthRepository(apiAuth: ApiAuth, storagePreferences: StoragePreferences) = AuthRepository(storagePreferences, apiAuth)
}