package kr.hs.dgsw.dbook.SignUp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import cn.pedant.SweetAlert.SweetAlertDialog
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.SignUp.Network.SignUpBody
import kr.hs.dgsw.dbook.SignUp.Network.SignUpResponse
import kr.hs.dgsw.dbook.network.DbookApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class SetupRetrofit {
    //네트워크 작업
    internal fun setupRetrofit(
        email: String,
        password: String,
        getApplication: Application,
        context: Context
    ) {

        //로딩 다이얼로그
        val sweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = Color.parseColor("#0DE930")
        sweetAlertDialog
            .setTitleText("로딩 중")
            .setCancelable(false)
        sweetAlertDialog.show()

        //signUp 서비스
        val signUpService =
            (getApplication as DBookApplication).retrofit.create(DbookApi::class.java)

        //signUp 서비스 결과 값
        signUpService.SignUp(SignUpBody(email, password))
            .enqueue(object : Callback<SignUpResponse> {
                val signUpDialog = SignDialog()

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    signUpDialog.connectionFail(context, sweetAlertDialog)
                }

                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    val intent = Intent(context, SignUpActivity::class.java)

                    signUpDialog.connectionSuccess(
                        response.code(),
                        response.message(),
                        context,
                        response.errorBody()?.string().toString(),
                        intent,
                        sweetAlertDialog
                    )

                }

            })
    }
}