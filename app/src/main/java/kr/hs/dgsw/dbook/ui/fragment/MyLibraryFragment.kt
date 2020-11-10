package kr.hs.dgsw.dbook.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.dbook.MyAdapter
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.model.EBookModel

class MyLibraryFragment : Fragment() {
    var bookList = arrayListOf<EBookModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val view = inflater.inflate(R.layout.fragment_book_list, container, false)
        val mAdapter = MyAdapter(requireContext(), bookList)
        val DRV: RecyclerView = view.findViewById(R.id.downloadRecycler)
        DRV.adapter = mAdapter
        //                Glide.with(context)
//                    .load(profile.image_urls)
//                    .into(userProfile2)
        return view

    }


}