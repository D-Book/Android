package kr.hs.dgsw.dbook.model

data class EBookModel(val id: String, val category: String,
                      val title: String, val author: String,
                      val book_file: String, val cover_image: String,
                      val description: String, val uploader_id: String,
                        val publisher: String, val published: String) {
}