package com.team1.volunteerapp.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
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
    val chatRoomList_Key = mutableListOf<String>()
    lateinit var chatRoomList_rvAdapter : ChatRoomListRVAdapter
    private val uidKey = FBAuth.getUid()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_list)

        // RecyclerView 연결
        val chatRoomList_rv = findViewById<RecyclerView>(R.id.mRecyclerViewChatRoomList)
        chatRoomList_rvAdapter = ChatRoomListRVAdapter(chatRoomList_items)
        chatRoomList_rv.adapter = chatRoomList_rvAdapter
        chatRoomList_rv.layoutManager = LinearLayoutManager(this)

        // 데이터 가져오기
        getChatRoomListData()

        val addBtn = findViewById<Button>(R.id.chatListAddBtn)
        addBtn.setOnClickListener {

            val Intent = Intent(this, ChatRoomMakerActivity::class.java)
            startActivity(Intent)
            chatRoomList_rvAdapter.notifyDataSetChanged()
        }

        chatRoomList_rvAdapter.itemClick = object : ChatRoomListRVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                val intent = Intent(chatRoomList_rv.context, ChatRoomInfoActivity::class.java)
                intent.putExtra("roomKey", chatRoomList_Key[position].toString())
                ContextCompat.startActivity(chatRoomList_rv.context, intent, null)
            }

        }

    }

    private fun getChatRoomListData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                chatRoomList_items.clear()

                for(dataModel in snapshot.children){
                    try {
                        val item = dataModel.getValue(ChatRoomInfoModel::class.java)
                        Log.d("asdfasdfa", item.toString())
                        if (item != null) {
                            chatRoomList_items.add(item)
                            chatRoomList_Key.add(dataModel.key.toString())
                        }
                    }catch (e : Exception){
                        Log.d("asdfasdfa", e.toString())
                    }

                }
                chatRoomList_items.reverse()
                chatRoomList_Key.reverse()
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