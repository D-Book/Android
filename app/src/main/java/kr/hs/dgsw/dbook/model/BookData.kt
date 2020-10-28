package kr.hs.dgsw.dbook.model

data class BookData(
        var data : ArrayList<BookDetailData>,
        var category_name: String = "",
        var category_id: String = ""
){
    companion object{
        var instance : BookListData? = null
    }
}