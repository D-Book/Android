package kr.hs.dgsw.dbook.network

import kr.hs.dgsw.dbook.SignUp.Network.SignUpBody
import kr.hs.dgsw.dbook.SignUp.Network.SignUpResponse
import kr.hs.dgsw.dbook.model.BookListData
import kr.hs.dgsw.dbook.model.LoginRequest
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.model.libraryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DBookApi {
    @POST("/user/login")
    fun login(@Body loginInfo: LoginRequest
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