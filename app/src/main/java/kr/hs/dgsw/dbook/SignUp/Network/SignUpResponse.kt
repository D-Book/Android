package kr.hs.dgsw.dbook.SignUp.Network

class SignUpResponse(
        var message: String?,
        var status: String?

) {

    companion object {
        var instance: SignUpResponse? = null
    }
}