package com.team1.volunteerapp.Chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class ChatRoomListRVAdapter(val items : MutableList<ChatRoomInfoModel>) : RecyclerView.Adapter<ChatRoomListRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomListRVAdapter.ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.chatroomlist_rv_item, parent, false)

        return ViewHolder(view1)
    }

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ChatRoomListRVAdapter.ViewHolder, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var chatTitile = itemView.findViewById<TextView>(R.id.chatListRvTitle)
        var chatSubTitile = itemView.findViewById<TextView>(R.id.chatListRvContents)
        var chatCreateUser = itemView.findViewById<TextView>(R.id.chatListRvUser)

        fun bindItems(item: ChatRoomInfoModel){


            chatTitile.text = item.chatInfo?.chatRoomTitle
            chatSubTitile.text = item.chatInfo?.chatRoomSubTitle
            chatCreateUser.text = item.chatInfo?.chatRoomMakerUid

        }

    }
}
