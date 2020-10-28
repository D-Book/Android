package kr.hs.dgsw.dbook.model

data class UserData(
        var id : String = "",
        var profile_image : String = "",
        var username : String = "",
        var email : String = "",
        var password : String = ""
){
    companion object{
        var instance : UserData? = null
    }
}