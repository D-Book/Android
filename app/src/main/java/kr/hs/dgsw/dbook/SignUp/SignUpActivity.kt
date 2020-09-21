package kr.hs.dgsw.dbook.SignUp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*
import kr.hs.dgsw.dbook.Login.LoginActivity
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.network.ApiManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher

class SignUpActivity : AppCompatActivity(){
    var checkEmail: Boolean = false
    var checkPassword: Boolean = false

    private val api = ApiManager.getInstance()
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
        SignPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkPassword()
            }

            override fun afterTextChanged(s: Editable) {
                checkPassword()
            }
        })
        backToLogin.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
        }
        btn_signUp.setOnClickListener{
            val SignupInfor = SignUpBody().apply {
                email = SignEmail.text.toString()
                password = this@SignUpActivity.SignPassword.text.toString()
            }
            api.SignUp(SignupInfor)
                    .enqueue(object : Callback<SignUpResponse> {
                        override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                            Log.d("TEST", "onFailure: " )

                        }

                        override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                            Log.d("Email","data: ${response.body()}")
                            if (response.code() == 400){

                            }
                        }

                    })
        }

    }
    private fun isEmail(email: String): Boolean {
        var returnValue = false
        val pattern = Patterns.EMAIL_ADDRESS
        val m: Matcher = pattern.matcher(email)
        if (m.matches()) {
            returnValue = true
        }
        return returnValue
    }

    private fun checkButton(checkEmail: Boolean, checkPassword: Boolean){

        this.checkEmail = checkEmail
        this.checkPassword = checkPassword

        Log.d("data1", "data: $checkEmail")
        Log.d("data1", "data: $checkPassword")

        if(checkEmail && checkPassword){

            btn_signUp.setBackgroundResource(R.drawable.bg_secondary_rounded_16dp)
            btn_signUp.isEnabled = true

        }else{

            btn_signUp.setBackgroundResource(R.drawable.bg_secondary_rounded_16dp)
            btn_signUp.isEnabled = false

        }
    }
    private fun checkEmail(){
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

    private fun checkPassword(){
        val regExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$".toRegex()
        if (SignPassword.text.toString().isNotEmpty() && SignPassword.text.toString().matches(regExp)) {
            Thread {
                runOnUiThread {
                    checkPWText.text = "올바른 비밀번호 형식입니다."
                    checkPWText.setTextColor(getColorStateList(R.color.colorBlue))
                    checkPassword = true
                    checkButton(checkEmail, checkPassword)
                }
            }.start()
        } else {
            Thread{
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


