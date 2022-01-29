package kg.iaau.diploma.local_storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import kg.iaau.diploma.data.About
import kg.iaau.diploma.data.Faq
import kg.iaau.diploma.data.MedCard

@Database(entities = [About::class, Faq::class, MedCard::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun aboutDao(): AboutDao
    abstract fun faqDao(): FaqDao
    abstract fun medCardDao(): MedCardDao

}