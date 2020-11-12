package kr.hs.dgsw.dbook.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
        var message: String? = "",
        var email: String? = "",
        var token: String? = "",
        @SerializedName("object")
        var update : ObjectData = ObjectData(null)
) {

    companion object {
        var instance: LoginResponse? = null
    }
}