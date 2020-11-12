package kr.hs.dgsw.dbook.model

data class ObjectData(
        var user : UserData?
){
companion object{
    var instance : ObjectData? = null
}
}