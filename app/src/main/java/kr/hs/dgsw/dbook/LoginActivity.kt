package kr.hs.dgsw.dbook

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kr.hs.dgsw.dbook.model.LoginRequest
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.network.DbookApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.80.162.210:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(DbookApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener{
            val email = editEmail.text.toString()
            val password = password.text.toString()
            val loginInfo= LoginRequest(email, password)
            api.login(loginInfo).enqueue(object : Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("TEST", "onFailure: "+t.message)
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

            })
        }

    }

}