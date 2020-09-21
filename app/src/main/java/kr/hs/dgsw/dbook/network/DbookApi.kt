package kr.hs.dgsw.dbook.network

import kr.hs.dgsw.dbook.SignUp.Network.SignUpBody
import kr.hs.dgsw.dbook.SignUp.Network.SignUpResponse
import kr.hs.dgsw.dbook.model.LoginRequest
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.model.libraryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DbookApi {
    @POST("/auth/login")
    fun login(@Body loginInfo: LoginRequest):Call<LoginResponse>

    @GET("/library")
    fun getLibrary(

    ):Call<libraryResponse>
    @POST("/users/activity_signup")
    fun SignUp(@Body SignUpInfo : SignUpBody):Call<SignUpResponse>
}