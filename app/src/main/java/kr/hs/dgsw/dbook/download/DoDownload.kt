package kr.hs.dgsw.dbook.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.hs.dgsw.dbook.WorkingNetwork.BaseUrl
import kr.hs.dgsw.dbook.local.DBookDatabase
import kr.hs.dgsw.dbook.local.entity.BookEntity
import kr.hs.dgsw.dbook.model.BookDetailData
import java.io.File

class DoDownload {
    val baseUrl = BaseUrl()
    internal fun doDownload(bookModel: BookDetailData, context: Context) {
        val bookDao = Room.databaseBuilder(context, DBookDatabase::class.java, "DBookDatabase")
                .build()
                .bookDao()
        val downloadManager: DownloadManager? = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        val uri = Uri.parse(baseUrl.resolve(bookModel.book_file))
        DownloadManager.Request(uri)
                .setTitle(bookModel.title)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(context, "Download", bookModel.id + ".epub").let {
                    val downloadId = downloadManager?.enqueue(it)
                    val book = BookEntity(bookModel.id,
                            bookModel.category,
                            bookModel.title,
                            bookModel.author,
                            File(context.getExternalFilesDir("Download"), bookModel.id + ".epub").path,
                            bookModel.cover_image,
                            bookModel.description,
                            bookModel.uploader_email,
                            bookModel.publisher,
                            bookModel.published,
                            downloadId!!)
                    GlobalScope.launch(Dispatchers.IO) {
                        bookDao.insert(book)
                    }
                }

    }
}