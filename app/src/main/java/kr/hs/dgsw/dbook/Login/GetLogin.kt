package kr.hs.dgsw.dbook.Login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.Dialog.LoginDialog
import kr.hs.dgsw.dbook.MainActivity
import kr.hs.dgsw.dbook.model.LoginRequest
import kr.hs.dgsw.dbook.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetLogin {
    internal fun getLogin(
            email: String,
            password: String,
            getApplication: Application,
            context: Context
    ) {
        //dialog
        val sweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = Color.parseColor("#0DE930")
        sweetAlertDialog
                .setTitleText("로딩 중")
                .setCancelable(false)
        sweetAlertDialog.show()

        val api = (getApplication as DBookApplication)
                .requestService()
        api?.login(LoginRequest(email, password))
                ?.enqueue(object : Callback<LoginResponse> {
                    val loginDialog = LoginDialog()
                    override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>

                    ) {

                        val intent = Intent(context, MainActivity::class.java)
                        //Dialog 창 로그인 시도시 결과에 따라 다른 dialog 가 뜬다
                        loginDialog.connectionSuccess(
                                response.code(),
                                context,
                                response.errorBody()?.string().toString(),
                                intent,
                                sweetAlertDialog
                        )

                        //통신성공
                        if (response.code() == 200) {

                            //서버로부터 받은 정보들을 EmailLoginBody 변수에 담아준다
                            LoginResponse.instance = response.body()

                        }
                        //통신 실패
                        else if (response.code() == 401) {

                        }

                    }

                    //서버와 연결 실패
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("s", "s")
                        //LoginDialog 를 호출하여 서버와의 연결 실패를 dialog 로 띄운다
                        loginDialog.connectionFail(context, sweetAlertDialog)
                    }


                })
    }
}

