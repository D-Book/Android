package kr.hs.dgsw.dbook.model

data class BookData(
        var data : ArrayList<BookDetailData>
){
    companion object{
        var instance : BookListData? = null
    }
}