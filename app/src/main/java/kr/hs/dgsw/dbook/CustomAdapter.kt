/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package kr.hs.dgsw.dbook



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.dbook.model.EBookModel


class CustomAdapter(private val dataSet: Array<String>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(v: View, BookTitle: View,ItemView: View) : RecyclerView.ViewHolder(v) {
        //recyclerview 에 올릴 프로토스 사진
        private val BookCover = EBookModel.instance?.image
        //recyclerview 에 올릴 프로토스 이름
        private val BookTitle = EBookModel.instance?.name
        //recyclerview 에 올릴 프로토스 나이

        fun bind (Book: EBookModel, context: Context) {
            if (Book.image != "") {
                val resourceId = context.resources.getIdentifier(Book.image, "drawable", context.packageName)
                BookCover?.setImageResource(resourceId)
            } else {
                BookCover?.setImageResource(R.mipmap.ic_launcher)
            }
            hisName?.text = Person.name
            hisAge?.text = Person.age
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val bookCover = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_book_list, viewGroup, false)
        val bookTitle = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_book_list, viewGroup, false)
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_book_list, viewGroup, false)


        return ViewHolder(bookCover,bookTitle,view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    companion object {
        private val TAG = "CustomAdapter"
    }
}
