package kr.hs.dgsw.dbook.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_category_detail.*
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.BookList.OnChangeFragmentListener
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.model.BookListData
import kr.hs.dgsw.dbook.ui.adapter.BookDetailDataAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val EXTRA_CATEGORY_ID = "category_name"

class CategoryDetailFragment : Fragment(R.layout.fragment_category_detail) {
    var categoryId: String? = null
    var onChangeFragmentListener: OnChangeFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onChangeFragmentListener = parentFragment as? OnChangeFragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getString(EXTRA_CATEGORY_ID)
        }
    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_back.setOnClickListener {

            parentFragmentManager.popBackStack()
        }
        (activity?.application as DBookApplication).requestService()?.getBookList()?.enqueue(object : Callback<BookListData> {
            override fun onFailure(call: Call<BookListData>, t: Throwable) {

            }

            override fun onResponse(call: Call<BookListData>, response: Response<BookListData>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (bookData in it.content) {
                            if (bookData.category_id == categoryId) {
                                txt_category.text = bookData.category_name
                                val adapter = BookDetailDataAdapter(bookData.books) {
                                    onChangeFragmentListener?.changeToBookDetail(it)
                                }
                                category.adapter = adapter
                            }
                        }
                    }
                }
            }
        })
    }
}