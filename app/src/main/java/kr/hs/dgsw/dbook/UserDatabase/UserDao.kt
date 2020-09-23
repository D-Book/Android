package kr.hs.dgsw.dbook.UserDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(UDB: UserDB?)

    @Query("DELETE FROM UserInformation")
    fun deleteAll()

    @Query("SELECT * from UserInformation ORDER BY email ASC")
    fun getAlphabetizedWords(): LiveData<List<UserDB?>?>?
}