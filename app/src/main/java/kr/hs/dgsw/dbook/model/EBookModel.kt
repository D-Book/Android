package kr.hs.dgsw.dbook.model

data class EBookModel(val id: Int,
                      val category: String,
                      val title: String,
                      val author: String,
                      val book_file: String,
                      val cover_image: String,
                      val description: String,
                      val uploader_id: Int,
                      val publisher: String,
                      val published: String) {
        companion object{
        var instance: EBookModel? = null
    }
}