package kr.hs.dgsw.dbook.ui.fragment


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kr.hs.dgsw.dbook.R

const val EXTRA_CATEGORY_NAME = "category_name"
class CategoryDetailFragment : Fragment(R.layout.fragment_category_detail){
    var categoryname :String?=null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryname=it.getString(EXTRA_CATEGORY_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}