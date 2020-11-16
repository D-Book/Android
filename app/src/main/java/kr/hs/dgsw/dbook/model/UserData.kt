package kr.hs.dgsw.dbook.model

data class UserData(
        var email : String = "",
        var password : String = "",
        var id : String = "",
        var profileImage : String? = "",
        var name : String = ""

){
    companion object{
        var instance : UserData? = null
    }
}