package kr.hs.dgsw.dbook.ui.fragment


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_category_detail.*
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.model.BookListData
import kr.hs.dgsw.dbook.ui.adapter.BookDetailDataAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val EXTRA_CATEGORY_NAME = "category_name"

class CategoryDetailFragment : Fragment(R.layout.fragment_category_detail) {
    var categoryId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getString(EXTRA_CATEGORY_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as DBookApplication).requestService()?.getBookList()?.enqueue(object : Callback<BookListData> {
            override fun onFailure(call: Call<BookListData>, t: Throwable) {

            }

            override fun onResponse(call: Call<BookListData>, response: Response<BookListData>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (bookData in it.content) {
                            if (bookData.category_id == categoryId) {
                                val adapter = BookDetailDataAdapter(context!!, bookData.data)
                                category.adapter = adapter
                            }
                        }
                    }
                }
            }
        })
    }
}