package kr.hs.dgsw.dbook.model

data class BookArrayData(
        var books : ArrayList<EBookModel>
){
    companion object{
        var instance : BookArrayData? = null
    }
}