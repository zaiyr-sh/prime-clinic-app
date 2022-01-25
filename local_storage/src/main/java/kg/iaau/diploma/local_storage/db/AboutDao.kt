package kg.iaau.diploma.local_storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kg.iaau.diploma.data.About

@Dao
interface AboutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAboutUsInfo(aboutList: List<About>)

    @Query("select * from About")
    fun getInfoAboutUsFromDb(): List<About>

}