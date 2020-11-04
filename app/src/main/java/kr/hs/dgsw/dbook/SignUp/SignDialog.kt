package kr.hs.dgsw.dbook.SignUp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
/*
* - 엑티비티: 회원가입 Dialog 엑티비티
* - 담당자: 한승재
* - 수정 날짜: 2020.09.22
*/
class SignDialog{

    internal fun connectionSuccess(
            responseCode: Int,
            context: Context,
            responseBody: String,
            intent: Intent,
            sweetAlertDialog: SweetAlertDialog
    ) {
        //로그인 성공
        when (responseCode) {
            200 -> {
                sweetAlertDialog.dismiss()
                val dialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)

                dialog.setCancelable(false)

                dialog.setTitleText("회원가입 성공")
                        .setConfirmClickListener {
                            ContextCompat.startActivity(context, intent, null)
                            (context as Activity).finish()
                            ActivityCompat.finishAffinity(context)
                        }
                        .show()

            }
            //로그인 실패
            412 -> {
                sweetAlertDialog.dismiss()
                val dialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)

                dialog.setCancelable(false)

                dialog.setTitleText("회원가입에 실패했습니다")
                        .setConfirmClickListener {
                            dialog.dismiss()
                        }
                        .setContentText(responseBody)
                        .show()
            }

            else -> {
            }
        }
    }
    //서버 통신 실패
    fun connectionFail(context: Context, sweetAlertDialog: SweetAlertDialog) {

        sweetAlertDialog.dismiss()
        val dialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)

        dialog.setCancelable(false)

        dialog.setTitleText("서버 통신에 실패하였습니다.")
                .setConfirmClickListener {
                    dialog.dismiss()
                }
                .setContentText("네트워크 연결을 확인해 주세요.")
                .show()
    }


}