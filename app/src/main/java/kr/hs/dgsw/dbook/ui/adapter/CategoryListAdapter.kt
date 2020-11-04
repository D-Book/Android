package kr.hs.dgsw.dbook.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category_list.view.*
import kr.hs.dgsw.dbook.R
import kr.hs.dgsw.dbook.model.CategoryData

class CategoryListAdapter(val categories: List<CategoryData>, val listener: OnClickListener?) : RecyclerView.Adapter<CategoryListAdapter.Holder>() {

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(category: CategoryData) {
            itemView.txt_category_name.text = category.category_name
            itemView.recycler_view_book_list.adapter = BookDetailDataAdapter(category.data, false) {
                listener?.onBookClick(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_list, parent, false)
        val holder = Holder(view)
        view.layout_category.setOnClickListener {
            listener?.onCategoryClick(categories[holder.adapterPosition].category_id)
        }
        return holder
    }

    override fun getItemCount(): Int = categories.size


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(categories[position])
    }

    interface OnClickListener {
        fun onCategoryClick(categoryId: String)

        fun onBookClick(bookId: String)
    }
}