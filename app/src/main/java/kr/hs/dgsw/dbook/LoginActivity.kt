package kr.hs.dgsw.dbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kr.hs.dgsw.dbook.SignUp.SignUpActivity
import kr.hs.dgsw.dbook.model.LoginRequest
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.network.ApiManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error

class LoginActivity : AppCompatActivity() {
    val api = ApiManager.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        intent_SignUp.setOnClickListener{
            val nextIntent = Intent(this, SignUpActivity::class.java)
            startActivity(nextIntent)
        }
        btn_login.setOnClickListener {
            val loginInfo = LoginRequest().apply {
                email = editEmail.text.toString()
                password = this@LoginActivity.password.text.toString()
            }
            api.login(loginInfo)
                    .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("TEST", "onFailure: " )

                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    LoginResponse.instance = response.body()
                    Log.d("Email","data: ${response.body()}")
                    if (response.code() == 401){
                        Log.d("fuck","fuck"+response.errorBody())
                    }
                }

            })
        }

    }

}
