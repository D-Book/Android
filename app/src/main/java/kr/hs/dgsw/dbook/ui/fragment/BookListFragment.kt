package kr.hs.dgsw.dbook.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.hs.dgsw.dbook.BookList.OnChangeFragmentListener
import kr.hs.dgsw.dbook.R

class BookListFragment : Fragment(), OnChangeFragmentListener {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                    .add(R.id.fcv, CategoryListFragment())
                    .commit()
        }
    }
    override fun changeToBookDetail(bookId: String) {
        childFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_fragment,0)
                .replace(R.id.fcv, BookDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_BOOK_ID, bookId)
                    }
                })
                .addToBackStack(null)
                .commit()
    }

    override fun changeToCategoryDetail(categoryId: String) {
        childFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_fragment,0)
                .replace(R.id.fcv, CategoryDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_CATEGORY_ID, categoryId)
                    }
                })
                .addToBackStack(null)
                .commit()
    }
}