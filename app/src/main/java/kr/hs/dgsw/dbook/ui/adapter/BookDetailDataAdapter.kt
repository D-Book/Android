package kr.hs.dgsw.dbook.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.WorkingNetwork.BaseUrl
import kr.hs.dgsw.dbook.model.BookDetailData

class BookDetailDataAdapter(val BookList: List<BookDetailData>, val grid: Boolean = true, val listener: (String) -> Unit) : RecyclerView.Adapter<BookDetailDataAdapter.Holder>() {

    val baseUrl = BaseUrl()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookDetailDataAdapter.Holder {
        val layoutId = when (grid) {
            true -> R.layout.item_book_list_2
            else -> R.layout.item_book_list
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val holder = Holder(view)
        view.setOnClickListener {
            listener.invoke(BookList[holder.adapterPosition].id)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return BookList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookCover: ImageView = itemView.findViewById(R.id.img_cover)
        var bookTitle: TextView = itemView.findViewById(R.id.txt_title)

        fun bind(book: BookDetailData) {
            Glide.with(itemView)
                    .load(baseUrl.resolve(book.cover_image))
                    .into(bookCover)
            bookTitle.text = book.title
        }
    }

    override fun onBindViewHolder(holder: BookDetailDataAdapter.Holder, position: Int) {
        holder.bind(BookList[position])
    }

}