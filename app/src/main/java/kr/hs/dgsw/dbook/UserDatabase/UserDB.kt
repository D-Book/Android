package kr.hs.dgsw.dbook.UserDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserInformation")
class UserDB{
    @PrimaryKey
    var Email: String? = null

    var profile_image: String? = null
    var library_image : String? = null
    var library_name : String? = null
}