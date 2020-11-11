package kr.hs.dgsw.dbook.model

class LoginRequest(
        var email: String = "",
        var password: String = "",
        var access_token:String = ""
         ) {

}