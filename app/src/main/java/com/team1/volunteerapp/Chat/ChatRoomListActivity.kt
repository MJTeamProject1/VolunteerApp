package com.team1.volunteerapp.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.team1.volunteerapp.Comment.CommentModel
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import java.lang.Exception
import java.util.*

class ChatRoomListActivity : AppCompatActivity() {
    val chatRoomList_items = mutableListOf<ChatRoomInfoModel>()
    lateinit var chatRoomList_rvAdapter : ChatRoomListRVAdapter
    private val uidKey = FBAuth.getUid()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_list)

        val chatRoomList_rv = findViewById<RecyclerView>(R.id.mRecyclerViewChatRoomList)

        chatRoomList_rvAdapter = ChatRoomListRVAdapter(chatRoomList_items)

        chatRoomList_rv.adapter = chatRoomList_rvAdapter
        chatRoomList_rv.layoutManager = LinearLayoutManager(this)

        // 데이터 가져오기
        chatRoomList_items.clear()
        getChatRoomListData()

        val addBtn = findViewById<Button>(R.id.chatListAddBtn)
        addBtn.setOnClickListener {

            val Intent = Intent(this, ChatRoomMakerActivity::class.java)
            startActivity(Intent)
            chatRoomList_items.clear()
            chatRoomList_rvAdapter.notifyDataSetChanged()
        }

    }

    private fun getChatRoomListData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(dataModel in snapshot.children){
                    try {
                        val item = dataModel.getValue(ChatRoomInfoModel::class.java)
                        Log.d("asdfasdfa", item.toString())
                        if (item != null) {
                            chatRoomList_items.add(item)
                        }
                    }catch (e : Exception){
                        Log.d("asdfasdfa", e.toString())
                    }

                }
                chatRoomList_rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        FBRef.chatRoomListRef.addValueEventListener(postListener)
    }


}