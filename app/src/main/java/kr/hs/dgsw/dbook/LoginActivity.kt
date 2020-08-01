package kr.hs.dgsw.dbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    val retrofit = Retrofit.Builder()
            .baseUrl("http://10.80.162.210:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
