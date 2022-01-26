package kg.iaau.diploma.local_storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kg.iaau.diploma.data.Faq

@Dao
interface FaqDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaqs(faqs: List<Faq>)

    @Query("select * from Faq")
    suspend fun getFaqFromDb(): List<Faq>

}