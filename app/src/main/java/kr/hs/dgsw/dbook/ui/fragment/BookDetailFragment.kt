package kr.hs.dgsw.dbook.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_book_detail.*
import kr.hs.dgsw.dbook.EXTRA_BOOK_ID
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.ViewerActivity
import kr.hs.dgsw.dbook.download.DoDownload
import kr.hs.dgsw.dbook.model.BookDetailData

const val EXTRA_BOOK_ID = "book_name"

class BookDetailFragment : Fragment(R.layout.fragment_book_detail) {

    val doDownload = DoDownload()
    var bookId : String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getString(EXTRA_BOOK_ID)
        }
    }
}