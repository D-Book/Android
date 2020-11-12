package kr.hs.dgsw.dbook.SignUp
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import kr.hs.dgsw.dbook.SignUp.RotateImage
import kr.hs.dgsw.dbook.SignUp.SetupRetrofit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

open class SignUpNameActivity : AppCompatActivity() {

    //서버로 보낼 데이터 변수
    private var email: String = ""
    private var password: String = ""
    private var userName: String = ""
    private var profileImage: RequestBody? = null
    private var imageName: String = ""


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
            setupRetrofit.setupRetrofit(email, password, userName, profileImage, imageName, application, this)
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




    //갤러리에서 넘어온 이미지 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val returnUri: Uri
        val returnCursor: Cursor?

        // 이미지 파일이 넘어 왔을 경우
        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {

                //이미지 정보
                returnUri = data?.data!!

                //이미지 파일 받아오기
                val inputStream = contentResolver.openInputStream(returnUri) //input 스트림
                var bm: Bitmap = BitmapFactory.decodeStream(inputStream) //비트맵 변환
                bm = rotateImageClass.rotateImage(data.data!!, bm, contentResolver) //이미지 회전
                val bos = ByteArrayOutputStream()
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                profileImage = RequestBody.create(MultipartBody.FORM, bos.toByteArray())
                inputStream?.close()

                //화면에 이미지 표시
                Glide.with(this)
                        .load(bm)
                        .thumbnail(Glide.with(applicationContext).load(R.raw.loading))
                        .transform(CenterCrop(), RoundedCorners(1000000000))
                        .into(profile_image)

                returnCursor = contentResolver.query(returnUri, null, null, null, null)

                //이미지 이름
                val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                imageName = returnCursor.getString(nameIndex)

                returnCursor.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //어떤 파일도 넘어오지 않았을 때
        else if (requestCode == 101 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show()
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
}