package kr.hs.dgsw.dbook.BookList

interface OnChangeFragmentListener {
    fun changeToBookDetail(bookId: String)

    fun changeToCategoryDetail(categoryId: String)
}