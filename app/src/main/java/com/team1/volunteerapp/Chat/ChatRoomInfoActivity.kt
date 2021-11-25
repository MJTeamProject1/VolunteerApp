package com.team1.volunteerapp.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBRef
import java.lang.Exception

class ChatRoomInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_info)

        val roomKey = intent.getStringExtra("roomKey")


        getGroupInfo(roomKey!!)


        val groupJoinBtn = findViewById<Button>(R.id.chatRoomJoinBtn)
        groupJoinBtn.setOnClickListener {

        }
    }

    private fun getGroupInfo(roomKey : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(ChatRoomInfoModel::class.java)

                var title = findViewById<TextView>(R.id.ChatRoomTitle)
                var content = findViewById<TextView>(R.id.ChatRoomSubTitle)

                title.text = dataModel?.chatInfo?.chatRoomTitle
                content.text = dataModel?.chatInfo?.chatRoomSubTitle
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        FBRef.chatRoomListRef.child(roomKey).addValueEventListener(postListener)
    }
}