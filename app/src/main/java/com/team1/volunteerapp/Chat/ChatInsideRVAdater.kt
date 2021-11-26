package com.team1.volunteerapp.Chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth

class ChatInsideRVAdater (val items : MutableList<ChatModel>): RecyclerView.Adapter<ChatInsideRVAdater.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInsideRVAdater.ViewHolder {
        when(viewType){
            0 -> {
                val view1 = LayoutInflater.from(parent.context).inflate(R.layout.chat_my_rv_item,parent,false)
                return ViewHolder(view1)
            }
            else ->{
                val view1 = LayoutInflater.from(parent.context).inflate(R.layout.chat_rv_item,parent,false)
                return ViewHolder(view1)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(items[position].uid == FBAuth.getUid()){
            0
        }else{
            1
        }

//        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ChatInsideRVAdater.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val chatText : TextView = itemView.findViewById(R.id.chatRvText)
        private val chatUser : TextView = itemView.findViewById(R.id.chatRvNickName)
        private val chatTime : TextView = itemView.findViewById(R.id.chatRvTime)

        fun bindItems(item :ChatModel){
            chatText.text = item.text
            chatUser.text = item.user
            chatTime.text = item.time
        }
    }
}