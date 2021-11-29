package com.team1.volunteerapp.Chat.ChatRoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.Chat.ChatJoinUserModel
import com.team1.volunteerapp.R

class ChatRoomInfoRVAdapter(val items : MutableList<ChatJoinUserModel>) : RecyclerView.Adapter<ChatRoomInfoRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.chatjoinuser_rv_item, parent, false)

        return ViewHolder(view1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val chatUser: TextView = itemView.findViewById(R.id.chatUserRvTitle)
        fun bindItems(item: ChatJoinUserModel){
            chatUser.text = item.nickname
        }
    }
}