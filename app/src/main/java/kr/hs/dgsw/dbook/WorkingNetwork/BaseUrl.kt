package kr.hs.dgsw.dbook.WorkingNetwork

data class BaseUrl(
        val BaseURL: String = "https://4978f607-03b5-4521-987a-2b93bc2af13a.mock.pstmn.io"
) {
    fun resolve(path: String): String {
        return BaseURL + when (path.startsWith("/")) {
            true -> path.substring(1)
            false -> path
        }
    }
}