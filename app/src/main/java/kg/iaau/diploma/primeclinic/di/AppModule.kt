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
import kg.iaau.diploma.network.api.*
import kg.iaau.diploma.primeclinic.repository.*
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
    fun provideAboutDao(db: AppDatabase) = db.aboutDao()

    @Provides
    fun provideFaqDao(db: AppDatabase) = db.faqDao()

    @Singleton
    @Provides
    fun providesStoragePreferences(@ApplicationContext context: Context) = StoragePreferences(context)

    @Provides
    fun providesAuthRepository(storagePreferences: StoragePreferences, apiAuth: ApiAuth) =
        AuthRepository(storagePreferences, apiAuth)

    @Provides
    fun providesAboutRepository(
        apiAbout: ApiAbout,
        aboutDao: AboutDao
    ) = AboutRepository(apiAbout, aboutDao)

    @Provides
    fun providesFaqRepository(apiFaq: ApiFaq, faqDao: FaqDao) = FaqRepository(apiFaq, faqDao)

    @Provides
    fun providesMedCardRepository(
        storagePreferences: StoragePreferences,
        apiMedCard: ApiMedCard
    ) = MedCardRepository(storagePreferences, apiMedCard)

    @Provides
    fun providesClinicRepository(storagePreferences: StoragePreferences, apiClinic: ApiClinic) =
        ClinicRepository(storagePreferences, apiClinic)

}