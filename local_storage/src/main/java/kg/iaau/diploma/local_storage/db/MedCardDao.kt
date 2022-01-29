package kg.iaau.diploma.local_storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kg.iaau.diploma.data.MedCard

@Dao
interface MedCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMedCardInDb(medCard: MedCard)

    @Query("SELECT * FROM MedCard")
    fun getMedCardFromDb(): MedCard

}