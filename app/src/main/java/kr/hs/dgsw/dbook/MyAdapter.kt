package kr.hs.dgsw.dbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_book_list.view.*
import kr.hs.dgsw.dbook.model.EBookModel

class MyAdapter (context: Context,private val BookList: List<EBookModel>): RecyclerView.Adapter<MyAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return BookList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BookList[position])
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookCover : ImageView = itemView.findViewById(R.id.img_cover)

        var bookTitle : TextView = itemView.findViewById(R.id.txt_title)

        fun bind (book: EBookModel) {
            Glide.with(itemView)
                    .load(book.cover_image)
                    .override(50,150)
                    .into(bookCover)
            bookTitle.text = book.title
        }
    }
}

