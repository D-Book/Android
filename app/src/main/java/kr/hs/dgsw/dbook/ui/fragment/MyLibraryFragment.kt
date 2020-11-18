package kr.hs.dgsw.dbook.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.sip.SipSession
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.TableInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.fragment_category_detail.*
import kotlinx.android.synthetic.main.fragment_category_list.*
import kotlinx.android.synthetic.main.fragment_my_library.*
import kotlinx.android.synthetic.main.fragment_my_library.view.*
import kotlinx.android.synthetic.main.item_book_list_2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.BookList.OnChangeFragmentListener
import kr.hs.dgsw.dbook.MyAdapter
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.WorkingNetwork.BaseUrl
import kr.hs.dgsw.dbook.getLibrary.GetMyLibrary
import kr.hs.dgsw.dbook.local.DBookDatabase
import kr.hs.dgsw.dbook.local.entity.BookEntity
import kr.hs.dgsw.dbook.model.*
import kr.hs.dgsw.dbook.ui.adapter.BookDetailDataAdapter
import kr.hs.dgsw.dbook.ui.adapter.BookEntityAdapter
import kr.hs.dgsw.dbook.ui.adapter.CategoryListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import java.lang.NullPointerException

class MyLibraryFragment : Fragment() {
    var bookList = listOf<BookEntity>()
    private val baseUrl = BaseUrl()
    var onChangeFragmentListener: OnChangeFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onChangeFragmentListener = parentFragment as? OnChangeFragmentListener
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d("Image","Image${LoginResponse.instance!!.update.user!!.profileImage}")
        // Log.d("profileImage","profileImage${LoginResponse.instance!!.update.user!!.profileImage?.let { baseUrl.resolve(it) }}")
        Glide.with(requireContext())
                .load(LoginResponse.instance!!.update.user!!.profile_image)
                .circleCrop()
                .into(img_profile)
        Log.d("image,", "image : ${LoginResponse.instance!!.update.user!!.profile_image}")

        Glide.with(requireContext())
                .load(R.drawable.librarybackground)
                .override(60, 60)
                .into(img_background)
        view.txt_library_name.text = "김첨지의 서재"
        view.txt_email.text = LoginResponse.instance!!.email
        //val getBook = GetBook(requireContext())
        //getBook.start()
        CoroutineScope(Dispatchers.IO).launch {
            bookList = DBookDatabase
                    .getDatabase(requireContext())!!
                    .bookDao()
                    .getAll()

            withContext(Dispatchers.Main) {
                view.txt_read_book.text = "${bookList.size} 권"
                Log.d("item", "item : $bookList 권")
            }
        }
        (activity?.application as DBookApplication).requestService()?.getLibrary()?.enqueue(object : Callback<libraryResponse> {
            override fun onFailure(call: Call<libraryResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<libraryResponse>, response: Response<libraryResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val adapter = BookDetailDataAdapter(it.bookList!!.books!!) {
                            onChangeFragmentListener?.changeToBookDetail(it)
                        }
                        downloadRecycler.adapter = adapter
                    }
                }
            }
        })
        my_library_btn.setOnClickListener {
            (activity?.application as DBookApplication).requestService()?.getLibrary()?.enqueue(object : Callback<libraryResponse> {
                override fun onFailure(call: Call<libraryResponse>, t: Throwable) {

                }

                override fun onResponse(call: Call<libraryResponse>, response: Response<libraryResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val adapter = BookDetailDataAdapter(it.bookList!!.books!!) {
                                onChangeFragmentListener?.changeToBookDetail(it)
                            }
                            downloadRecycler.adapter = adapter
                        }
                    }
                }
            })
        }
        upload_btn.setOnClickListener {
            (activity?.application as DBookApplication).requestService()?.uploadLibrary()?.enqueue(object : Callback<libraryResponse> {
                override fun onFailure(call: Call<libraryResponse>, t: Throwable) {

                }

                override fun onResponse(call: Call<libraryResponse>, response: Response<libraryResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val adapter = BookDetailDataAdapter(it.bookList!!.books!!) {
                                onChangeFragmentListener?.changeToBookDetail(it)
                            }
                            downloadRecycler.adapter = adapter
                        }
                    }
                }
            })
        }
        download_btn.setOnClickListener{
            val adapter = BookEntityAdapter(bookList) {
                onChangeFragmentListener?.changeToBookDetail(it)
            }
            downloadRecycler.adapter = adapter
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val activityBox = requireActivity()
        val getMyLibrary = GetMyLibrary()
        val view = inflater.inflate(R.layout.fragment_my_library, container, false)
        getMyLibrary.getLibrary(activityBox.application)
        return view
    }
}

