package kg.iaau.diploma.local_storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import kg.iaau.diploma.data.About
import kg.iaau.diploma.data.Faq

@Database(entities = [About::class, Faq::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun aboutDao(): AboutDao
    abstract fun faqDao(): FaqDao

}