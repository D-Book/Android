package kr.hs.dgsw.dbook.network

import kr.hs.dgsw.dbook.model.LoginRequest
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.model.libraryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DbookApi {
    @POST("/users/logins")
    fun login(@Body loginInfo: LoginRequest):Call<LoginResponse>

    @GET("/library")
    fun getLibrary(

    ):Call<libraryResponse>
}