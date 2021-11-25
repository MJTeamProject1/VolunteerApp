package com.team1.volunteerapp.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef

class ChatRoomMakerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_maker)

        val addBtn = findViewById<Button>(R.id.chatRoomCreateBtn)
        addBtn.setOnClickListener {
            var isGoToJoin = true
            val title = findViewById<EditText>(R.id.ChatRoomTitle)
            val content = findViewById<EditText>(R.id.ChatRoomSubTitle)

            val inputTitle = title.text.toString()
            val inputContent = content.text.toString()

            if(inputTitle.isEmpty()){
                Toast.makeText(this,"그룹 명을 입력하지 않았습니다",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            else if(inputContent.isEmpty()){
                Toast.makeText(this,"내용을 입력하지 않았습니다",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            else{
                if(isGoToJoin){
                    inputChatRoomListData(inputTitle, inputContent)
                    Toast.makeText(this, "생성 완료", Toast.LENGTH_SHORT).show()
                    val Intent = Intent(this, ChatRoomListActivity::class.java)
                    startActivity(Intent)
                    finish()
                }
            }
        }
    }

    private fun inputChatRoomListData(title : String, content : String){

        FBRef.chatRoomListRef
            .push()
            .setValue(
                ChatRoomInfoModel(
//                    ChatJoinUserModel(
//                        FBAuth.getUid(),
//                        FBAuth.getUserData(4)
//                    ),
                    ChatRoomListModel(
                        0,
                        2,
                        title,
                        content,
                        FBAuth.getUserData(4))
                )
            )
    }
}