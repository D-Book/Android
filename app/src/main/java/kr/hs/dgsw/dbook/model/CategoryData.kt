package kr.hs.dgsw.dbook.model

data class CategoryData(
        var books : ArrayList<BookDetailData>,
        var category_name: String = "",
        var category_id: String = ""
){
    companion object{
        var instance : BookListData? = null
    }
}