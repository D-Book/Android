package kr.hs.dgsw.dbook.model

data class BookDetailData(
        var id : String = "",
        var category : String = "",
        var title : String = "",
        var author : String = "",
        var book_file : String = "",
        var cover_image : String = "",
        var description : String = "",
        var uploader_id : String = "",
        var publisher : String = "",
        var published : String =  ""
){
    companion object {
        var instance: BookDetailData? = null
    }
}