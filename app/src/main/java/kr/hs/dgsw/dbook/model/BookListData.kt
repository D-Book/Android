package kr.hs.dgsw.dbook.model

import com.google.gson.annotations.SerializedName

data class BookListData(
        var code: String = "",
        var message: String = "",
        @SerializedName("object")
        var content: ArrayList<CategoryData>

){
    companion object
    {
        var instance : BookListData? = null
    }
}