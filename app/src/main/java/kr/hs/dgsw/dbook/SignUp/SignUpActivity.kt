package kr.hs.dgsw.dbook.SignUp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editEmail
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_register.*
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.model.LoginRequest
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.network.ApiManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity(){
    val api = ApiManager.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btn_signUp.setOnClickListener{
            val SignupInfor = SignUpBody().apply {
                email = SignEmail.text.toString()
                password = this@SignUpActivity.Signpassword.text.toString()
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
}