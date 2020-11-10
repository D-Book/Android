package kr.hs.dgsw.dbook.ui.fragment

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_book_detail.*
import kotlinx.android.synthetic.main.fragment_book_detail.txt_category
import kotlinx.android.synthetic.main.fragment_book_detail.view.*
import kotlinx.android.synthetic.main.fragment_category_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.ViewerActivity
import kr.hs.dgsw.dbook.WorkingNetwork.BaseUrl
import kr.hs.dgsw.dbook.download.DoDownload
import kr.hs.dgsw.dbook.local.DBookDatabase
import kr.hs.dgsw.dbook.local.dao.BookDao
import kr.hs.dgsw.dbook.model.BookDetailData
import kr.hs.dgsw.dbook.model.BookListData
import java.util.Calendar.getInstance

const val EXTRA_BOOK_ID = "book_name"

class BookDetailFragment : Fragment(R.layout.fragment_book_detail) {

    val baseUrl = BaseUrl()
    val doDownload = DoDownload()
    var bookId: String? = null
    var book: BookDetailData? = null

    var bookDao: BookDao? = null

    val receiver = object:BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            setButton()
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
            parentFragmentManager.popBackStack() }
        btn_delete.setOnClickListener {
            val deleteBook = DeleteBook(requireContext())
            deleteBook.start()
        }
       //txt_publisher.text = BookDetailData.instance!!.publisher
        //Log.e("pub","pub: ${BookDetailData.instance!!.publisher}")
        BookListData.instance?.content?.forEach {
            val category = it
            it.data.forEach {
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
            txt_date.text = published
            Glide.with(this@BookDetailFragment)
                    .load(baseUrl.resolve(cover_image))
                    .into(img_cover)
            setButton()
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(receiver)
    }

    fun setButton(){
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

            }
        }

    }

}
class DeleteBook(val context: Context) : Thread() {
    override fun run() {
        /*DBookDatabase
                .getDatabase(context)!!
                .bookDao()
                .delete()*/
    }
}
