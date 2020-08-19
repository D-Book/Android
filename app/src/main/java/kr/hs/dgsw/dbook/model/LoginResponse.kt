package kr.hs.dgsw.dbook.model

import com.google.gson.annotations.SerializedName

class LoginResponse(
        var message: String?,
        var email: String?,
        var token: String?

)