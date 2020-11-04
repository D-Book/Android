package kr.hs.dgsw.dbook.Applacation

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import kr.hs.dgsw.dbook.WorkingNetwork.BaseUrl
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.network.DBookApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DBookApplication : Application(){
    private var loginInterface: DBookApi? = null
    lateinit var retrofit: Retrofit
    private var baseUrl : BaseUrl = BaseUrl()


    override fun onCreate() {
        super.onCreate()
        val interceptor = Interceptor() {
            val token = LoginResponse.instance?.token
            val newRequest: Request
            if (token != null && !token.equals("")) { // 토큰이 없는 경우
                // Authorization 헤더에 토큰 추가
                newRequest = it.request().newBuilder().addHeader("Token", token).build()
            } else newRequest = it.request()
            it.proceed(newRequest)
        }
        Stetho.initializeWithDefaults(this)

        val httpClient = OkHttpClient.Builder()
        httpClient.interceptors().add(interceptor)
        httpClient.addNetworkInterceptor(StethoInterceptor())
        val client = httpClient.build()

         retrofit = Retrofit.Builder()
            .baseUrl(baseUrl.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
             .client(client)
            .build()
        loginInterface = retrofit.create(DBookApi::class.java)

    }
    fun requestService(): DBookApi?{
        return loginInterface
    }
}