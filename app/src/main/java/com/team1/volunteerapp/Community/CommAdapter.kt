package com.team1.volunteerapp.Community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R
class CommAdapter(val items: MutableList<BoardModel>) : RecyclerView.Adapter<CommAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comm_card_rv_item, parent, false)
        return CustomViewHolder(view)//추가
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }
    var itemClick : ItemClick? = null

    inner class CustomViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        private val title = itemView.findViewById<TextView>(R.id.rvTitle)
        private val nickname = itemView.findViewById<TextView>(R.id.rvNickName)
        private val contents = itemView.findViewById<TextView>(R.id.rvContents)
        private val time = itemView.findViewById<TextView>(R.id.rvTime)
        fun bindItems(item: BoardModel){
            title.text = item.title
            nickname.text = item.nickname
            contents.text = item.content
            time.text=item.time

        }
    }
}
