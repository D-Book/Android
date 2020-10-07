package kr.hs.dgsw.dbook.model

data class EBookModel(val no: Int, val uploader: Int,
                      val name: String, val genre: String,
                      val author: String, val uploadDate: String,
                      val image: String, val preview: String,
                        val isSharable: String, val path: String) {
    companion object{
        var instance: EBookModel? = null
    }
}