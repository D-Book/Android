package kr.hs.dgsw.dbook.network

import kr.hs.dgsw.dbook.model.LoginRequest
import kr.hs.dgsw.dbook.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DbookApi {
    @POST("users/logins")
    fun login(@Body loginInfo: LoginRequest):Call<LoginResponse>
}