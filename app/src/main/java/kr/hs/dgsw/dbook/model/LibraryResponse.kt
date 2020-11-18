package kr.hs.dgsw.dbook.model

import com.google.gson.annotations.SerializedName

data class LibraryResponse (
    var code : Int = 0,
    var message : String? = null,
    @SerializedName("object")
    var bookList : ArrayList<BookArrayData>? = null
){
    companion object{
        var instance : LibraryResponse? = null
    }
}