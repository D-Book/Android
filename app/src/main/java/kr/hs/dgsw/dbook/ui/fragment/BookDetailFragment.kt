package kr.hs.dgsw.dbook.ui.fragment

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.room.Room
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_book_detail.*
import kotlinx.android.synthetic.main.fragment_book_detail.txt_category
import kotlinx.android.synthetic.main.fragment_book_detail.view.*
import kotlinx.android.synthetic.main.fragment_category_detail.*
import kotlinx.android.synthetic.main.fragment_category_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.hs.dgsw.dbook.Applacation.DBookApplication
import kr.hs.dgsw.dbook.Dialog.LoginDialog
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.ViewerActivity
import kr.hs.dgsw.dbook.WorkingNetwork.BaseUrl
import kr.hs.dgsw.dbook.download.DoDownload
import kr.hs.dgsw.dbook.local.DBookDatabase
import kr.hs.dgsw.dbook.local.dao.BookDao
import kr.hs.dgsw.dbook.local.entity.BookEntity
import kr.hs.dgsw.dbook.model.*
import kr.hs.dgsw.dbook.ui.adapter.CategoryListAdapter
import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar.getInstance

const val EXTRA_BOOK_ID = "book_name"

class BookDetailFragment : Fragment(R.layout.fragment_book_detail) {
    private lateinit var callback: OnBackPressedCallback
    val baseUrl = BaseUrl()
    val doDownload = DoDownload()
    var bookId: String? = null
    var book: BookDetailData? = null

    var bookDao: BookDao? = null

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val downloadManager: DownloadManager? = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
            downloadManager?.let {
                val c = it.query(DownloadManager.Query().setFilterById(downloadId))
                val columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                if (c.moveToFirst()) {
                    when (c.getInt(columnIndex)) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            // 다운로드 성공
                            setButton()
                        }
                        else -> {
                            // 다운로드 실패
                            Toast.makeText(context, "다운로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getString(EXTRA_BOOK_ID)
        }
        bookDao = Room.databaseBuilder(requireContext(), DBookDatabase::class.java, "DBookDatabase")
                .build()
                .bookDao()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back_book_btn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        //txt_publisher.text = BookDetailData.instance!!.publisher
        //Log.e("pub","pub: ${BookDetailData.instance!!.publisher}")
        BookListData.instance?.content?.forEach {
            val category = it
            it.books.forEach {
                if (it.id.equals(bookId)) {
                    book = it
                    txt_category.text = category.category_name
                }
            }
        }
        book?.run {
            txt_title.text = title
            txt_author.text = author
            txt_explain.text = description
            txt_publisher.text = publisher

            // txt_date.text = feedPostTime.calFeedTime(publisher)
            Glide.with(this@BookDetailFragment)
                    .load(cover_image)
                    .override(460, 620)
                    .into(img_cover)
            setButton()
        }
        add_mybook_btn.setOnClickListener {
            (activity?.application as DBookApplication).requestService()?.myLibrary(AddLibraryData(bookId!!))?.enqueue(object : Callback<MyLibraryResponse> {
                override fun onFailure(call: Call<MyLibraryResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<MyLibraryResponse>, response: Response<MyLibraryResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "내 서재 추가 완료", Toast.LENGTH_SHORT).show();
                    }
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(receiver)
    }

    fun setButton() {
        CoroutineScope(Dispatchers.IO).launch {

            val bookEntity = bookDao?.getBookById(bookId!!)
            launch(Dispatchers.Main) {
                if (bookEntity == null) {
                    btn_delete.isEnabled = false
                    btn_download_read.setText(R.string.download)
                    btn_download_read.setOnClickListener {
                        doDownload.doDownload(book!!, requireContext())
                    }
                } else {
                    btn_delete.isEnabled = true
                    btn_download_read.setText(R.string.read_now);
                    btn_download_read.setOnClickListener {
                        startActivity(Intent(requireContext(), ViewerActivity::class.java).apply {
                            putExtra(kr.hs.dgsw.dbook.EXTRA_BOOK_ID, bookId!!)
                        })
                    }

                }
                btn_delete.setOnClickListener {
                    val deleteBook = DeleteBook(requireContext(), bookId)
                    deleteBook.start()
                    btn_delete.isEnabled = false
                    btn_download_read.setText(R.string.download)
                    btn_download_read.setOnClickListener {
                        doDownload.doDownload(book!!, requireContext())
                    }
                }
            }
        }
    }
}

class DeleteBook(val context: Context, val bookId: String?) : Thread() {
    override fun run() {
        DBookDatabase
                .getDatabase(context)!!
                .bookDao()
                .deleteById(bookId!!)
        Log.e("test", "test : $bookId")
        BookDetailFragment()
    }
}


