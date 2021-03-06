package com.team1.volunteerapp.Chat.ChatRoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class ChatRoomListRVAdapter(val items : MutableList<ChatRoomInfoModel>) : RecyclerView.Adapter<ChatRoomListRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.chatroomlist_rv_item, parent, false)

        return ViewHolder(view1)
    }

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

        private val chatTitile: TextView = itemView.findViewById(R.id.chatListRvTitle)
        private val chatSubTitile: TextView = itemView.findViewById(R.id.chatListRvContents)
        private val chatCreateUser: TextView = itemView.findViewById(R.id.chatListRvUser)
        private val chatCount : TextView = itemView.findViewById(R.id.chatCount)
        private val chatImg : ImageView = itemView.findViewById(R.id.imageView)
        fun bindItems(item: ChatRoomInfoModel){
            chatTitile.text = item.chatInfo?.chatRoomTitle
            chatSubTitile.text = item.chatInfo?.chatRoomSubTitle
            chatCreateUser.text = item.chatInfo?.chatRoomMakerNickName
            chatCount.text = item.chatInfo?.chatRoomMaxUnit.toString() + "명"

            var imaNumber = item.chatInfo?.chatRoomNumber
            when(imaNumber){
                1-> chatImg.setImageResource(R.drawable.cooperation)
                2-> chatImg.setImageResource(R.drawable.fist)
                3-> chatImg.setImageResource(R.drawable.savenature)
                4-> chatImg.setImageResource(R.drawable.dove)

            }
        }

    }
}
