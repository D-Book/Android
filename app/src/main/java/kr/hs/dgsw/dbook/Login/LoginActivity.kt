package kr.hs.dgsw.dbook.Login
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.SignUp.SignUpActivity

/*
* - 엑티비티: 로그인 엑티비티(이메일, 비밀번호)
* - 담당자: 한승재
* - 수정 날짜: 2020.09.22
*/

class LoginActivity : AppCompatActivity() {
    private val getLogin = kr.hs.dgsw.dbook.Login.GetLogin()

    //사용자의 이메일을 받을 변수
    private var email: String = ""

    //사용자의 비밀번호를 받을 변수
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        intent_signUp.setOnClickListener{
            val nextIntent = Intent(this, SignUpActivity::class.java)
            startActivity(nextIntent)
        }
        btn_login.setOnClickListener {
                //사용자의 이메일을 email 변수에 담아준다
                email = this@LoginActivity.editEmail.text.toString()
                //입력받은 비밀번호를 password 변수에 담아준다
             //   password = layout_password?.text.toString()
                getLogin.getLogin(email, password, application, this@LoginActivity)
        }

    }

}
