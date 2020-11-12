package kr.hs.dgsw.dbook.WorkingNetwork

data class BaseUrl(
        val BaseURL: String = "http://10.80.163.63:3000/"
) {
    fun resolve(path: String): String {
        return BaseURL + when (path.startsWith("/")) {
            true -> path.substring(1)
            false -> path
        }
    }
}