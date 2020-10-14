package kr.hs.dgsw.dbook.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.dbook.MyAdapter
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.model.EBookModel

class MyLibraryFragment : Fragment(R.layout.fragment_my_library) {
    var BookList = arrayListOf<EBookModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = MyAdapter(requireContext(), BookList)
        val DRV : RecyclerView = view.findViewById(R.id.downloadRecycler)
        DRV.adapter = mAdapter
    }
}