package kr.hs.dgsw.dbook.SignUp

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream

class Base64Encoding {

    internal fun encoding(format: String, bitmap: Bitmap, context: Context): String {

        var base64 = ""

        Log.d("data7", "data: $format")

        when (format) {

            "jpg" -> {

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                base64 = "data:image/jpg;base64,${Base64.encodeToString(byteArray, Base64.NO_WRAP)}"
                Log.d("base64Data", "base64: $base64")

            }

            "jpeg" -> {

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                base64 = "data:image/jpeg;base64,${Base64.encodeToString(byteArray, Base64.NO_WRAP)}"
                Log.d("base64Data", "base64: $base64")

            }

            "png" -> {

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                base64 = "data:image/png;base64,${Base64.encodeToString(byteArray, Base64.NO_WRAP)}"
                Log.d("base64Data", "base64: $base64")

            }

            else -> {
                Toast.makeText(context,"제대로 된 파일을 넣어주세요", Toast.LENGTH_LONG).show()
            }
        }

        return base64
    }

}