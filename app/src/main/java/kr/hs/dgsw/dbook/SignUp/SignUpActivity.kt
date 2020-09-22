package kr.hs.dgsw.dbook.SignUp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.view.*
import kr.hs.dgsw.dbook.Login.LoginActivity
import kr.hs.dgsw.dbook.R
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

    //setupRetrofit 클래스 변수
    private val setupRetrofit = SetupRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        SignEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkEmail()
            }

            override fun afterTextChanged(s: Editable) {
                checkEmail()
            }
        })

        SignPassWord.addTextChangedListener(object : TextWatcher {
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
        backToLogin.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
        }
        //회원가입하기 버튼을 누르면 처리해주는 함수
        btn_signUp.setOnClickListener {
            //입력받은 이메일을 문자열로 바꿔준다
            email = layout_password.text.toString()
            //입력받은 password 를 문자열로 바꿔준다
            password = layout_password.text.toString()
            //setupRetrofit 함수로 값을 전달해서 회원가입을 처리한다
            setupRetrofit.setupRetrofit(email, password, application, this)
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
        } else {

            btn_signUp.setBackgroundResource(R.drawable.bg_secondary_rounded_16dp)
            btn_signUp.isEnabled = false

        }
    }
    //입력받은 이메일 형식에 따라 레이아웃에 결과를 띄어주는 함수
    private fun checkEmail() {
        if (isEmail(SignEmail.text.toString())) {
            Thread {
                runOnUiThread {
                    checkEmailText.text = "올바른 이메일 형식입니다."
                    checkEmailText.setTextColor(getColorStateList(R.color.colorBlue))
                    checkEmail = true
                    checkButton(checkEmail, checkPassword)
                }
            }.start()
        } else {
            Thread {
                runOnUiThread {
                    checkEmailText.text = "올바르지 않은 이메일 형식입니다."
                    checkEmailText.setTextColor(getColorStateList(R.color.colorRed))
                    checkEmail = false
                    checkButton(checkEmail, checkPassword)
                }
            }.start()
        }
    }
    //입력받은 PassWord 가 형식에 맞는지 판단 해주는 함수
    private fun checkPassword() {
        val regExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$".toRegex()
        if (SignPassWord.text.toString().isNotEmpty() && SignPassWord.text.toString().matches(regExp)) {
            Thread {
                runOnUiThread {
                    checkPWText.text = "올바른 비밀번호 형식입니다."
                    checkPWText.setTextColor(getColorStateList(R.color.colorBlue))
                    checkPassword = true
                    checkButton(checkEmail, checkPassword)
                }
            }.start()
        } else {
            Thread {
                runOnUiThread {
                    checkPWText.text = "올바르지 않은 비밀번호 형식입니다."
                    checkPWText.setTextColor(getColorStateList(R.color.colorRed))
                    checkPassword = false
                    checkButton(checkEmail, checkPassword)
                }
            }.start()
        }
    }
}



