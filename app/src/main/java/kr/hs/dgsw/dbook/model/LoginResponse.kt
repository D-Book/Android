package kr.hs.dgsw.dbook.model

import com.google.gson.annotations.SerializedName

class LoginResponse(
        var message: String?,
        var email: String?,
        var token: String?,
        @SerializedName("object")
        var update : ObjectData
) {

    companion object {
        var instance: LoginResponse? = null
    }
}