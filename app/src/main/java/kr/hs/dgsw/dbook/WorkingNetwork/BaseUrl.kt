package kr.hs.dgsw.dbook.WorkingNetwork

data class BaseUrl(
        val BaseURL: String = "http://192.168.0.139:3000/"
) {
    fun resolve(path: String): String {
        return BaseURL + when (path.startsWith("/")) {
            true -> path.substring(1)
            false -> path
        }
    }
}