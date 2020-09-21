package kr.hs.dgsw.dbook.Applacation

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import kr.hs.dgsw.dbook.WorkingNetwork.BaseUrl
import kr.hs.dgsw.dbook.network.DbookApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DBookApplication : Application(){
    private var loginInterface: DbookApi? = null
    lateinit var retrofit: Retrofit
    private var baseUrl : BaseUrl = BaseUrl()

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        val httpClient = OkHttpClient.Builder()
        httpClient.addNetworkInterceptor(StethoInterceptor())
        val client = httpClient.build()

         retrofit = Retrofit.Builder()
            .baseUrl(baseUrl.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
             .client(client)
            .build()
        loginInterface = retrofit.create(DbookApi::class.java)
    }
    fun requestService(): DbookApi?{
        return loginInterface
    }
}