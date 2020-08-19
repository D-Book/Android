package kr.hs.dgsw.dbook.model

data class EBookModel(val ebookId: Int, val ebookTitle: String,
                      val ebookSubTitle: String, val ebookGenre: String,
                      val ebookAuthor: String, val ebookUploader: String,
                      val ebookFilePath: String, val ebookPosterPath: String) {
}