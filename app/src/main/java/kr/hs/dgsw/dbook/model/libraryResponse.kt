package kr.hs.dgsw.dbook.model

import com.google.gson.annotations.SerializedName

data class libraryResponse (
    var status : Int = 0,
    var message : String? = null,
    @SerializedName("object")
    var bookList : LibraryObjectData? = null
){
    companion object{
        var instance : libraryResponse? = null
    }
}