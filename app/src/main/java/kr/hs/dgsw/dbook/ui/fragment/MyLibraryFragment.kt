package kr.hs.dgsw.dbook.ui.fragment

import android.annotation.SuppressLint
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.fragment_my_library.*
import kotlinx.android.synthetic.main.fragment_my_library.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d("Image","Image${LoginResponse.instance!!.update.user!!.profileImage}")
        // Log.d("profileImage","profileImage${LoginResponse.instance!!.update.user!!.profileImage?.let { baseUrl.resolve(it) }}")
        Glide.with(requireContext())
                .load(LoginResponse.instance!!.update.user!!.profile_image)
                .circleCrop()
                .into(img_profile)
        Log.d("profile","profile : ${LoginResponse.instance!!.update.user!!.profile_image}")
        Log.d("image","image : ${LoginResponse.instance!!.update.user!!.profile_image}")

        Glide.with(requireContext())
                .load(R.drawable.librarybackground)
                .into(img_background)
        view.txt_library_name.text = "김첨지의 서재"
        view.txt_email.text = LoginResponse.instance!!.email
        //val getBook = GetBook(requireContext())
        //getBook.start()
        CoroutineScope(Dispatchers.IO).launch {
            val items = DBookDatabase
                    .getDatabase(requireContext())!!
                    .bookDao()
                    .getAll()

            withContext(Dispatchers.Main) {
            view.txt_read_book.text = "${items.size} 권"
                Log.d("item","item : $items 권")


            }
        }


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

