package kr.hs.dgsw.dbook.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BookDownloadBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val downloadManager: DownloadManager? = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
        downloadManager?.let {
            val c = it.query(DownloadManager.Query().setFilterById(downloadId))
            val columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (c.moveToFirst()) {
                when (c.getInt(columnIndex)) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        // 다운로드 성공
                    }
                    else -> {
                        // 다운로드 실패
                    }
                }
            }
        }
    }
}