package kr.hs.dgsw.dbook.network

import kr.hs.dgsw.dbook.SignUp.Network.SignUpBody
import kr.hs.dgsw.dbook.SignUp.Network.SignUpResponse
import kr.hs.dgsw.dbook.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DBookApi {
    @POST("/user/login")
    fun login(@Body loginInfo: UserData
    ):Call<LoginResponse>

    @GET("/library")
    fun getLibrary(
    ):Call<libraryResponse>

    @POST("/user/sign-up")
    fun signUp(@Body SignUpInfo : SignUpBody):Call<SignUpResponse>

    @GET("/ebook/list")
    fun getBookList(
    ):Call<BookListData>
}