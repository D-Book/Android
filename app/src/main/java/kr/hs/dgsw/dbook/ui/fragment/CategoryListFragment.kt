package kr.hs.dgsw.dbook.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_category_list.*
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.BookList.OnChangeFragmentListener
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.model.BookListData
import kr.hs.dgsw.dbook.ui.adapter.CategoryListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryListFragment : Fragment(), CategoryListAdapter.OnClickListener {

    var onChangeFragmentListener: OnChangeFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onChangeFragmentListener = parentFragment as? OnChangeFragmentListener
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as DBookApplication).requestService()?.getBookList()?.enqueue(object : Callback<BookListData> {
            override fun onFailure(call: Call<BookListData>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<BookListData>, response: Response<BookListData>) {
                if (response.isSuccessful)
                    recycler_view_categories.adapter = CategoryListAdapter(response.body()?.content!!, this@CategoryListFragment)
            }
        })
    }

    override fun onCategoryClick(categoryId: String) {
        onChangeFragmentListener?.changeToCategoryDetail(categoryId)
    }

    override fun onBookClick(bookId: String) {
        onChangeFragmentListener?.changeToBookDetail(bookId)
    }
}