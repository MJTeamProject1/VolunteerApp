package com.team1.volunteerapp.Chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.Chat.ChatRoom.ChatRoomListModel
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef

class ChatInsideRVAdater(
    val items: MutableList<ChatModel>,
    Key: String?,
    val chatCount: MutableList<Int>
): RecyclerView.Adapter<ChatInsideRVAdater.ViewHolder>(){
    val roomKey = Key
    var chatuserCount = 0
    var list = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInsideRVAdater.ViewHolder {
        when(viewType){
            0 -> {
                val view1 = LayoutInflater.from(parent.context).inflate(R.layout.chat_my_rv_item,parent,false)
                return ViewHolder(view1, roomKey)
            }
            else ->{
                val view1 = LayoutInflater.from(parent.context).inflate(R.layout.chat_rv_item,parent,false)
                return ViewHolder(view1, roomKey)
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
        holder.bindItems(items[position], chatCount[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView: View, roomKey: String?) : RecyclerView.ViewHolder(itemView){
        private val chatText : TextView = itemView.findViewById(R.id.chatRvText)
        private val chatUser : TextView = itemView.findViewById(R.id.chatRvNickName)
        private val chatTime : TextView = itemView.findViewById(R.id.chatRvTime)
        private val chatReadCount : TextView = itemView.findViewById(R.id.chatRVReadCount)
        private val roomKey = roomKey

        fun bindItems(item: ChatModel, chatCount: Int){

            chatText.text = item.text
            chatUser.text = item.user
            chatTime.text = item.time
            val readCount = item.readusercount

            val postListener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(dataModel in snapshot.children) {
                        val item = dataModel.getValue(ChatRoomListModel::class.java)
                        val item2 = item?.chatRoomMaxUnit
                        if (item?.chatRoomNumber != 0) {
                            Log.d("RVRVRVRV", item.toString())
                            Log.d("RVRVRV", item2.toString())
                            if (item2 != null) {
                                chatuserCount = item2
                            }
                            Log.d("BBBBBB", chatuserCount.toString())
                            Log.d("BBBBBB00", chatCount.toString())
                            chatReadCount.text = (chatuserCount-readCount).toString()
                            if((chatuserCount-readCount) == 0){
                                chatReadCount.visibility = View.INVISIBLE
                            }else{
                                chatReadCount.visibility = View.VISIBLE
                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }

            if (roomKey != null) {
                FBRef.chatRoomListRef.child(roomKey).addValueEventListener(postListener)
            }
        }
    }
}