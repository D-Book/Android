package kr.hs.dgsw.dbook.SignUp

import kr.hs.dgsw.dbook.model.LoginResponse

class SignUpResponse(
        var message: String?,
        var status: String?

) {

    companion object {
        var instance: SignUpResponse? = null
    }
}