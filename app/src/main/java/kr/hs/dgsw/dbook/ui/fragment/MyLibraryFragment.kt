package kr.hs.dgsw.dbook.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.TableInfo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_my_library.*
import kotlinx.android.synthetic.main.fragment_my_library.view.*
import kr.hs.dgsw.dbook.MyAdapter
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.WorkingNetwork.BaseUrl
import kr.hs.dgsw.dbook.local.DBookDatabase
import kr.hs.dgsw.dbook.model.EBookModel
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.model.UserData
import java.lang.NullPointerException

class MyLibraryFragment : Fragment() {
    var bookList = arrayListOf<EBookModel>()
    private val baseUrl = BaseUrl()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d("Image","Image${LoginResponse.instance!!.update.user!!.profileImage}")
        // Log.d("profileImage","profileImage${LoginResponse.instance!!.update.user!!.profileImage?.let { baseUrl.resolve(it) }}")
        Glide.with(requireContext())
                .load(R.drawable.background)
                .into(img_background)

        Glide.with(requireContext())
                .load(R.drawable.pngegg)
                .circleCrop()
                .into(img_profile)
        view.txt_library_name.text = "김첨지의 서재"
        view.txt_email.text = LoginResponse.instance!!.email
        val getBook = GetBook(requireContext())
        getBook.start()
     //   view.txt_s_saved_book.text =

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
class GetBook(val context : Context) : Thread() {
    override fun run() {
        val items = DBookDatabase
                .getDatabase(context)!!
                .bookDao()
                .getAll()

        Log.d("item","item : ${items.size}")
    }
}
