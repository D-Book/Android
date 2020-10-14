package kr.hs.dgsw.dbook

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio

fun Uri.asMultipart(name: String, contentResolver: ContentResolver): MultipartBody.Part? {
    return contentResolver.query(this, null, null, null, null, null)?.let {
        if (it.moveToNext()) {
            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? {
                    return MediaType.parse(contentResolver.getType(this@asMultipart)!!)
                }
                override fun writeTo(sink: BufferedSink) {
                    sink.writeAll(Okio.source(contentResolver.openInputStream(this@asMultipart)!!))
                }
            }
            MultipartBody.Part.createFormData(name, it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME)), requestBody)
        } else {
            null
        }
    }
}