package kr.hs.dgsw.dbook.SignUp

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.activity_sign_up_name.*
import kr.hs.dgsw.dbook.R
import java.lang.Boolean


/*
* - 엑티비티: 회원가입 엑티비티(프로필 사진, 이름)
* - 담당자: 양준혁
* - 수정 날짜: 2020.08.24
*/


//이름을 입력하고 서버에 전송을 담당하는 엑티비티
open class SignUpNameActivity : AppCompatActivity() {

    //서버로 보낼 데이터 변수
    private var email: String = ""
    private var password: String = ""
    private var userName: String = ""
    private var base64: String = ""

    //서버 통신을 할때 필요한 클래스
    private val base64Encoding = Base64Encoding() //Base64 인코딩
    private val setupRetrofit = SetupRetrofit() //retrofit setup
    private val rotateImageClass = RotateImage() //이미지 회전


    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_name)
        //엑션바 숨기기
        val actionBar = supportActionBar
        actionBar?.hide()
        //intent 데이터
        val intent: Intent = intent //이메일 비밀번호 인텐트 데이터
        email = intent.extras?.getString("userInfoEmail").toString() //이메일 저장
        password = intent.extras?.getString("userInfoPassword").toString() //password 저장
        //sha256 암호화


        //갤러리에서 프로필 사진 가져오기
        profile_image.setOnClickListener {
            val imageIntent = Intent() //구글 갤러리 접근 intent 변수
            //구글 갤러리 접근
            imageIntent.type = "image/*"
            imageIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(imageIntent, 101)

        }

        //뒤로 가기 버튼 눌렀을 때
        back_sign_up.setOnClickListener {
            //뒤로 돌아감
            onBackPressed()
        }

        //sign_up 버튼을 누르면 모든 값을 서버로 전송
        btn_start_dbook.setOnClickListener {
            setupRetrofit.setupRetrofit(email, password, base64, userName, application, this)
        }

        //이름이 null 인지 아닌지 판단
        input_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                userName = input_name.text.toString()

            }

            override fun afterTextChanged(s: Editable) {
                userName = input_name.text.toString()

            }
        })

    }

    //이름이 입력 될 때 마다 호출되는 함수(이름 형식 검사)


    //갤러리에서 넘어온 이미지 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val returnUri: Uri
        val returnCursor: Cursor?
        var imageSize = 0
        var imageNameFormat = ""

        //정상적인 응답이 왔을때
        if (requestCode == 101 && resultCode == RESULT_OK){
            //이미지 정보
            returnUri = data?.data!!

            //이미지 커서
            returnCursor = contentResolver.query(returnUri, null, null, null, null)

            //이미지 포멧
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val imageName = returnCursor.getString(nameIndex)
            val imageData = imageName.split(".")
            imageNameFormat = imageData[(imageData.size - 1)]

            //이미지 용량 사이즈
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            imageSize = returnCursor.getInt(sizeIndex) / 1024000
            returnCursor.close()
        }

        // 이미지 파일이 넘어 왔을 경우
        if (requestCode == 101 && resultCode == RESULT_OK && imageSize < 10) {
            try {
                //이미지 파일 받아오기
                val inputStream = contentResolver.openInputStream(data?.data!!) //input 스트림
                var bm: Bitmap = BitmapFactory.decodeStream(inputStream) //비트맵 변환
                inputStream?.close()

                bm = rotateImageClass.rotateImage(data.data!!, bm, contentResolver) //이미지 회전

                //이미지 resize
                bm = bitmapResizePrc(bm, 180, 180)

                //화면에 이미지 표시
                Glide.with(this)
                    .load(bm)
                    .thumbnail(Glide.with(applicationContext).load(R.raw.loading))
                    .override(1000)
                    .transform(CenterCrop(), RoundedCorners(1000000000))
                    .into(profile_image)

                base64 = "" //base64 초기화

                //base64 인코딩
                base64 = base64Encoding.encoding(imageNameFormat, bm, applicationContext)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //어떤 파일도 넘어오지 않았을 때
        else if (requestCode == 101 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show()
        }

        //파일 크기가 10메가 이상일 때
        else {
            Toast.makeText(this, "파일 최대 크기는 10MB 입니다.", Toast.LENGTH_LONG).show()
        }
    }

    //권한 허용 확인
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {

            val length = permissions.size

            for (i in 0..length) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "권환 허용" + permissions[i])
                }
            }
        }
    }


    //resize 이미지 crop
    private fun bitmapResizePrc(Src: Bitmap, newHeight: Int, newWidth: Int): Bitmap {

        var width = Src.width
        var height = Src.height

        // calculate the scale - in this case = 0.4f
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        // create matrix for the manipulation
        val matrix = Matrix()
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)

        // recreate the new Bitmap
        val resizedBitmap = Bitmap.createBitmap(Src, 0, 0, width, height, matrix, true)

        // check
        width = resizedBitmap.width
        height = resizedBitmap.height
        Log.i(
            "ImageResize", "Image Resize Result : " + Boolean.toString(
                newHeight == height && newWidth == width
            )
        )

        return resizedBitmap
    }
}