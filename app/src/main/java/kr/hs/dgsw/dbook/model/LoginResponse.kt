package kr.hs.dgsw.dbook.model

class LoginResponse(
        var message: String?,
        var email: String?,
        var token: String?

) {

    companion object {
        var instance: LoginResponse? = null
    }
}