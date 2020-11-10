package kr.hs.dgsw.dbook.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_my_library.*
import kr.hs.dgsw.dbook.MyAdapter
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.model.EBookModel
import java.lang.NullPointerException

class MyLibraryFragment : Fragment() {
    var bookList = arrayListOf<EBookModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext())
                .load(R.drawable.background)
                .into(img_background)
        Glide.with(requireContext())
                .load(R.drawable.pngegg)
                .into(img_profile)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val view = inflater.inflate(R.layout.fragment_my_library, container, false)
        val mAdapter = MyAdapter(requireContext(), bookList)
        val DRV: RecyclerView = view.findViewById(R.id.downloadRecycler)
        DRV.adapter = mAdapter





        return view

    }


}