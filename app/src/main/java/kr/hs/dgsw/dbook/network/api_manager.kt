package kr.hs.dgsw.dbook.network

import androidx.annotation.NonNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException

class api_manager {

    companion object {
        fun getinstance(): DbookApi{

            return Retrofit.Builder()
                    .baseUrl("http://10.80.162.210:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(DbookApi::class.java)
            var interceptor = Interceptor() {

        override fun intercept(@NonNull (Interceptor.Chain) chain):okhttp3.Response{
        var token = User.getInstance().getAccessToken();
        Request newRequest;
        if (token != null && !token.equals("")) { // 토큰이 없는 경우
            // Authorization 헤더에 토큰 추가
            newRequest = chain.request().newBuilder().addHeader("Authorization", token).build();

        } else newRequest = chain.request();
        return chain.proceed(newRequest);
    }
};

OkHttpClient.Builder builder = new OkHttpClient.Builder();
builder.interceptors().add(interceptor);
OkHttpClient client = builder.build();
retrofitBuilder.client(client);

Retrofit retrofit = retrofitBuilder.build();
RestApi api = retrofit.create(restApi.class);




        }




    }
}