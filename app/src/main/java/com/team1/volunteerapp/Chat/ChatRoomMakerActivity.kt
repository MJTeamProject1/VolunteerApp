package com.team1.volunteerapp.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_chat_room_maker.*

class ChatRoomMakerActivity : AppCompatActivity() {
    private var radioSelect = 1
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


            // 안됨 고쳐야 됨
            radioGroupVol.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.radio1 -> {
                        radioSelect = 1
                        Toast.makeText(this,"asdf",Toast.LENGTH_SHORT).show()
                    }
                    R.id.radio2 -> {
                        radioSelect = 2
                        Toast.makeText(this,"asdf22",Toast.LENGTH_SHORT).show()
                    }
                    R.id.radio3 -> {
                        radioSelect = 3
                        Toast.makeText(this,"asdf33",Toast.LENGTH_SHORT).show()
                    }
                    R.id.radio4 -> {
                        radioSelect = 4
                        Toast.makeText(this,"asdf44",Toast.LENGTH_SHORT).show()
                    }

                }
            }


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
                        radioSelect,
                        0,
                        title,
                        content,
                        FBAuth.getUserData(4))
                )
            )
    }

    inner class RadioListener : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(p0: RadioGroup?, p1: Int) { // p1 사용자가 선택한 라디오 버튼의 아이디값
            when (p0?.id) {
                R.id.radioGroupVol ->
                    when(p1){
                        R.id.radio1 -> { radioSelect = 1 }
                        R.id.radio2 -> { radioSelect = 2 }
                        R.id.radio3 -> { radioSelect = 3 }
                        R.id.radio4 -> { radioSelect = 4 }
                    }
                }
        }
    }
}