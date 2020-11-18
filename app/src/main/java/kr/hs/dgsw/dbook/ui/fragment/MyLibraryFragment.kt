package kr.hs.dgsw.dbook.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
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
import kr.hs.dgsw.dbook.getLibrary.GetMyLibrary
import kr.hs.dgsw.dbook.local.DBookDatabase
import kr.hs.dgsw.dbook.model.EBookModel
import kr.hs.dgsw.dbook.model.LoginResponse
import kr.hs.dgsw.dbook.model.UserData
import java.lang.NullPointerException

class MyLibraryFragment : Fragment() {
    var bookList = arrayListOf<EBookModel>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d("Image","Image${LoginResponse.instance!!.update.user!!.profileImage}")
        // Log.d("profileImage","profileImage${LoginResponse.instance!!.update.user!!.profileImage?.let { baseUrl.resolve(it) }}")
        Glide.with(requireContext())
                .load(LoginResponse.instance!!.update.user!!.profile_image)
                .circleCrop()
                .into(img_profile)

        Glide.with(requireContext())
                .load(R.drawable.librarybackground)
                .override(60,60)
                .into(img_background)
        view.txt_library_name.text = LoginResponse.instance!!.update.user!!.name + "의 서재"
        view.txt_email.text = LoginResponse.instance!!.email

        CoroutineScope(Dispatchers.IO).launch {
            val items = DBookDatabase
                    .getDatabase(requireContext())!!
                    .bookDao()
                    .getAll()

            withContext(Dispatchers.Main) {
            view.txt_read_book.text = "${items.size} 권"

            }
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val activityBox = requireActivity()
        val getMyLibrary = GetMyLibrary()
        val view = inflater.inflate(R.layout.fragment_my_library, container, false)
        val mAdapter = MyAdapter(requireContext(), bookList)
        val DRV: RecyclerView = view.findViewById(R.id.downloadRecycler)
        DRV.adapter = mAdapter
        getMyLibrary.getLibrary(activityBox.application)
        return view
    }
}

