package kr.hs.dgsw.dbook.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
class BookEntity(
        @PrimaryKey var id: String = "",
        var category: String = "",
        var title: String = "",
        var author: String = "",
        var book_file: String = "",
        var cover_image: String = "",
        var description: String = "",
        var uploader_email: String = "",
        var publisher: String = "",
        var published: String = "",
        var downloadId: Long,
        var isDownloaded: Boolean = false
) {

}