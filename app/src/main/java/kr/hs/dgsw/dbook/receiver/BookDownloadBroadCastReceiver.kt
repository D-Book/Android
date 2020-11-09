package kr.hs.dgsw.dbook.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.hs.dgsw.dbook.local.DBookDatabase

class BookDownloadBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bookDao = Room.databaseBuilder(context!!, DBookDatabase::class.java, "DBookDatabase")
                .build()
                .bookDao()
        val downloadManager: DownloadManager? = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
        CoroutineScope(Dispatchers.IO).launch {
            downloadManager?.let {
                val c = it.query(DownloadManager.Query().setFilterById(downloadId))
                val columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                if (c.moveToFirst()) {
                    when (c.getInt(columnIndex)) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            // 다운로드 성공
                            bookDao.updateDownloadSuccess(downloadId)
                        }
                        else -> {
                            // 다운로드 실패
                            bookDao.deleteByDownloadId(downloadId)
                        }
                    }
                }
            }
        }
    }
}