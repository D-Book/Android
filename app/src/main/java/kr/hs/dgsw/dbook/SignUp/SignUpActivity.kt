package kr.hs.dgsw.dbook.SignUp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.activity_sign_up_name.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.emailId
import kr.hs.dgsw.dbook.Login.LoginActivity
import kr.hs.dgsw.dbook.R
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.util.regex.Matcher


/*
* - 엑티비티: 회원가입 엑티비티(이메일, 비밀번호)
* - 담당자: 한승재
* - 수정 날짜: 2020.09.22
*/
class SignUpActivity : AppCompatActivity() {
    //입력받은 email 값을 저장할 변수
    var email: String = ""
    //입력받은 password 값을 저장할 변수
    var password: String = ""

    var checkEmail: Boolean = false

    var checkPassword: Boolean = false
    private var imageName: String = ""
    private var profileImage: RequestBody? = null
    private val rotateImageClass = RotateImage() //이미지 회전
    //setupRetrofit 클래스 변수
    private val setupRetrofit = SetupRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        btn_signUp.setBackgroundResource(R.drawable.bg_secondary_rounded_16dp)
        btn_signUp.isEnabled = false

        Sign_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkEmail()
            }

            override fun afterTextChanged(s: Editable) {
                checkEmail()
            }
        })

        passwordId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkPassword()
            }

            override fun afterTextChanged(s: Editable) {
                checkPassword()
            }
        })
        //로그인하기 text 를 누를시 LoginActivity 로 되돌아감
        back_to_login.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
        }
        //회원가입하기 버튼을 누르면 처리해주는 함수
        btn_signUp.setOnClickListener {
            //입력받은 이메일을 문자열로 바꿔준다
            email = Sign_email?.text.toString()
            //입력받은 password 를 문자열로 바꿔준다
            password = passwordId?.text.toString()
            val nextIntent = Intent(this,SignUpNameActivity::class.java)
            intent.putExtra("userInfoEmail", email)
            intent.putExtra("userInfoPassword", password)
            startActivity(nextIntent)
        }
    }
    //이메일이 정규식에 따라 알맞는지 확인 해주는 함수
    private fun isEmail(email: String): Boolean {
        var returnValue = false
        val pattern = Patterns.EMAIL_ADDRESS
        val m: Matcher = pattern.matcher(email)
        if (m.matches()) {
            returnValue = true
        }
        return returnValue
    }
    //조건에 따라 회원가입 버튼 활성화&비활성화 함수
    private fun checkButton(checkEmail: Boolean, checkPassword: Boolean) {

        this.checkEmail = checkEmail
        this.checkPassword = checkPassword

        //입력한 password 와 email 이 조건에 맞을시 회원가입 버튼 활성화
        if (checkEmail && checkPassword) {
            btn_signUp.setBackgroundResource(R.drawable.bg_secondary_rounded_16dp)
            btn_signUp.isEnabled = true
        //입력한 password 와 email 이 조건에 맞지 않을시 회원가입 버튼 비활성화
        }
        else {
            btn_signUp.setBackgroundResource(R.drawable.bg_secondary_rounded_16dp)
            btn_signUp.isEnabled = false
        }
    }
    //입력받은 이메일 형식에 따라 레이아웃에 결과를 띄어주는 함수
    private fun checkEmail() {
        if (isEmail(Sign_email.text.toString())) {

                    emailId.endIconDrawable = getDrawable(R.drawable.ic_register_correct)
                    emailId.isActivated = true
                    checkEmail = true
                    checkButton(checkEmail, checkPassword)

        } else {
                    emailId.endIconDrawable = getDrawable(R.drawable.ic_register_failed)
                    emailId.isActivated = false
                    checkEmail = false
                    checkButton(checkEmail, checkPassword)
        }
    }
    //입력받은 PassWord 가 형식에 맞는지 판단 해주는 함수
    @SuppressLint("ResourceType")
    private fun checkPassword() {
        val regExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$".toRegex()
        if (passwordId.text.toString().isNotEmpty() && passwordId.text.toString().matches(regExp)) {

                    layout_password.endIconDrawable = getDrawable(R.drawable.ic_register_correct)
                    checkPassword = true
                    checkButton(checkEmail, checkPassword)

        } else {
                    layout_password.endIconDrawable = getDrawable(R.drawable.ic_register_failed)
                    checkPassword = false
                    checkButton(checkEmail, checkPassword)
        }
    }
}



