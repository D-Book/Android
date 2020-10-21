package kr.hs.dgsw.dbook.SignUp

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

class RotateImage {

    @RequiresApi(api = Build.VERSION_CODES.N)
    internal fun rotateImage(uri: Uri, bitmap: Bitmap, contentResolver: ContentResolver) : Bitmap {

        val inputStream = contentResolver.openInputStream(uri)
        val exifInterface = ExifInterface(inputStream!!)
        inputStream.close()

        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                matrix.postRotate(90F)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                matrix.postRotate(180F)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.postRotate(270F)
            }
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    }
}