package kg.iaau.diploma.primeclinic.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kg.iaau.diploma.core.constants.DATABASE_NAME
import kg.iaau.diploma.local_storage.db.AboutDao
import kg.iaau.diploma.local_storage.db.AppDatabase
import kg.iaau.diploma.local_storage.db.FaqDao
import kg.iaau.diploma.local_storage.prefs.StoragePreferences
import kg.iaau.diploma.network.api.ApiAbout
import kg.iaau.diploma.network.api.ApiAuth
import kg.iaau.diploma.network.api.ApiFaq
import kg.iaau.diploma.primeclinic.repository.AboutRepository
import kg.iaau.diploma.primeclinic.repository.AuthRepository
import kg.iaau.diploma.primeclinic.repository.FaqRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAboutDao(db: AppDatabase) = db.aboutDao()

    @Provides
    @Singleton
    fun provideFaqDao(db: AppDatabase) = db.faqDao()

    @Singleton
    @Provides
    fun providesStoragePreferences(@ApplicationContext context: Context) = StoragePreferences(context)

    @Singleton
    @Provides
    fun providesAuthRepository(apiAuth: ApiAuth, storagePreferences: StoragePreferences) = AuthRepository(storagePreferences, apiAuth)

    @Singleton
    @Provides
    fun providesAboutRepository(apiAbout: ApiAbout, aboutDao: AboutDao) = AboutRepository(apiAbout, aboutDao)

    @Singleton
    @Provides
    fun providesFaqRepository(apiFaq: ApiFaq, faqDao: FaqDao) = FaqRepository(apiFaq, faqDao)
}