package com.team1.volunteerapp.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.Comment.CommentModel
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import java.lang.Exception

class ChatRoomInfoActivity : AppCompatActivity() {
    val userData = mutableListOf<ChatJoinUserModel>()
    val userDataCheck = mutableListOf<String>()
    var updateGroupCount = 0
    lateinit var chatUserJoinInfo_rvAdapter : ChatRoomInfoRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_info)

        // 그룹 고유 키 값 가져오기
        val roomKey = intent.getStringExtra("roomKey")

        //Recycler view 연결
        val chatRoomInfo_rv = findViewById<RecyclerView>(R.id.chatRoomInfoJoinRV)
        chatUserJoinInfo_rvAdapter = ChatRoomInfoRVAdapter(userData)
        chatRoomInfo_rv.adapter = chatUserJoinInfo_rvAdapter
        chatRoomInfo_rv.layoutManager = GridLayoutManager(this,2)

        // 그룹 정보 가져오기
        getGroupInfo(roomKey!!)

        // 그룹 가입 버튼 눌렀을 시
        val groupJoinBtn = findViewById<Button>(R.id.chatRoomJoinBtn)
        groupJoinBtn.setOnClickListener {
            joinGroupUserData(roomKey!!)
        }

        // 그룹 채팅 입장 버튼
        val groupChatBtn = findViewById<Button>(R.id.chatJoinBtn)
        groupChatBtn.setOnClickListener {
            val intent  = Intent(this, ChatInsideActivity::class.java)
            intent.putExtra("roomKey", roomKey)
            startActivity(intent)
        }
    }

    private fun getGroupInfo(roomKey : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(ChatRoomInfoModel::class.java)

                var title = findViewById<TextView>(R.id.ChatRoomTitle)
                var content = findViewById<TextView>(R.id.ChatRoomSubTitle)
                var groupCountInfo = findViewById<TextView>(R.id.groupCountInfo)

                title.text = dataModel?.chatInfo?.chatRoomTitle
                content.text = dataModel?.chatInfo?.chatRoomSubTitle
                groupCountInfo.text = dataModel?.chatInfo?.chatRoomMaxUnit.toString()+"명"
                updateGroupCount = dataModel?.chatInfo?.chatRoomMaxUnit!!

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        val postListener2 = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userData.clear()
                for(dataModel2 in snapshot.children){
                    val item = dataModel2.getValue(ChatJoinUserModel::class.java)
//                    Log.d("======asd=====", item.toString())
//                    if (item != null) {
//                        Log.d("======asd=====", item.nickname)
//                    }
                    userData.add(item!!)
                    userDataCheck.add(item.uid)
                }
                chatUserJoinInfo_rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        //key 값만 가져옴
        FBRef.chatRoomListRef.child(roomKey).addValueEventListener(postListener)
        FBRef.chatRoomListRef.child(roomKey).child("chatJoinUser").addValueEventListener(postListener2)
    }

    private fun joinGroupUserData(roomKey : String){
        if(!userDataCheck.contains(FBAuth.getUid())) {
            FBRef.chatRoomListRef
                .child(roomKey)
                .child("chatJoinUser")
                .push()
                .setValue(
                    ChatJoinUserModel(
                        FBAuth.getUid(),
                        FBAuth.getUserData(4)
                    )
                )



            val childUpdates = hashMapOf<String, Any>(
                "$roomKey/chatInfo/chatRoomMaxUnit" to updateGroupCount+1
            )

            FBRef.chatRoomListRef.updateChildren(childUpdates)

        }else{
            Toast.makeText(this,"이미 가입된 그룹입니다.",Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}