package kr.hs.dgsw.dbook.network

import kr.hs.dgsw.dbook.model.LoginResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    companion object {

        fun getInstance(): DbookApi {


            val retrofitBuilder = Retrofit.Builder()
                    .baseUrl("http://10.80.162.210:8080/")
                    .addConverterFactory(GsonConverterFactory.create())


            val interceptor = Interceptor() {
                val token = LoginResponse.instance?.token
                val newRequest: Request
                if (token != null && !token.equals("")) { // 토큰이 없는 경우
                    // Authorization 헤더에 토큰 추가
                    newRequest = it.request().newBuilder().addHeader("Authorization", token).build()
                } else newRequest = it.request()
                it.proceed(newRequest)
            }

            val builder = OkHttpClient.Builder()
            builder.interceptors().add(interceptor)
            builder.addNetworkInterceptor(StethoInterceptor())
            val client = builder.build();
            retrofitBuilder.client(client);


            val retrofit = retrofitBuilder.build();
            return retrofit.create(DbookApi::class.java)
        }
    }

}