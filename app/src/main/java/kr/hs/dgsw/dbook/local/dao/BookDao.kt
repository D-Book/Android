package kr.hs.dgsw.dbook.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.hs.dgsw.dbook.local.entity.BookEntity

@Dao
interface BookDao {
    @Query("select * from book")
    fun getAll(): List<BookEntity>

    @Query("select * from book where id = :id")
    fun getBookById(id: String): BookEntity?

    @Query("select isDownloaded from book where id = :id")
    fun isDownloadedBook(id: String): Boolean

    @Query("update book set isDownloaded = 1 where downloadId = :downloadId")
    fun updateDownloadSuccess(downloadId: Long)

    @Insert
    fun insert(bookEntity: BookEntity)

    @Delete
    fun delete(bookEntity: BookEntity)

    @Query("delete from book where downloadId = :downloadId")
    fun deleteByDownloadId(downloadId: Long)
}