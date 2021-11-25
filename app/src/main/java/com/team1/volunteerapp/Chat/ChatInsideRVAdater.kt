package com.team1.volunteerapp.Chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class ChatInsideRVAdater (val items : MutableList<ChatModel>): RecyclerView.Adapter<ChatInsideRVAdater.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInsideRVAdater.ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.chat_rv_item,parent,false)

        return ViewHolder(view1)
    }

    override fun getItemViewType(position: Int): Int {
//        items.
//        val chatMessage: ChatMessage = items[position]
//        Log.d("FFFFFF", "겟아이템뷰타입 : " + chatMessage.getuId())
//        Log.d("FFFFFF", "겟아이템뷰타입 내 토큰 : $mProfileUid")
//        return if (chatMessage.getuId().equals(mProfileUid)) { // 내 아이디인 경우 오른쪽뷰로 분기 (0)
//            0
//        } else { // 왼쪽뷰 (1)
//            1
//        }
        return super.getItemViewType(position)
    }
    override fun onBindViewHolder(holder: ChatInsideRVAdater.ViewHolder, position: Int) {

        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val chatText : TextView = itemView.findViewById(R.id.chatRvText)
        fun bindItems(item :ChatModel){
            chatText.text = item.text
        }
    }
}