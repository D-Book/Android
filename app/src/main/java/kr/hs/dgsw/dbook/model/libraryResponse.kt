package kr.hs.dgsw.dbook.model

class libraryResponse {
    var status : Int = 0
    var message : String? = null
    var books : List<EBookModel>? = null
}