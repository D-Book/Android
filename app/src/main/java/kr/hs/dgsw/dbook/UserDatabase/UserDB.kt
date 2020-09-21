package kr.hs.dgsw.dbook.UserDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserInformation")
class UserDB{
    @PrimaryKey
    var email: String? = null

    var profileImage: String? = null
    var libraryImage : String? = null
    var libraryName : String? = null
}