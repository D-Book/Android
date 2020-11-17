package kr.hs.dgsw.dbook.getLibrary

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.Dialog.LoginDialog
import kr.hs.dgsw.dbook.MainActivity
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.model.UserData
import kr.hs.dgsw.dbook.model.libraryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetMyLibrary (){
    internal fun getLibrary(
            getApplication: Application

    ) {

        val accessToken = LoginResponse.instance!!.token
        val api = (getApplication as DBookApplication)
                .requestService()
        api?.getLibrary(accessToken)
                ?.enqueue(object : Callback<libraryResponse> {

                    override fun onResponse(
                            call: Call<libraryResponse>,
                            response: Response<libraryResponse>

                    ) {
                        //통신성공
                        if (response.code() == 200) {
                            libraryResponse.instance = response.body()

                        }
                        //통신 실패
                        else if (response.code() == 401) {

                        }

                    }

                    //서버와 연결 실패
                    override fun onFailure(call: Call<libraryResponse>, t: Throwable) {


                    }


                })
    }
}