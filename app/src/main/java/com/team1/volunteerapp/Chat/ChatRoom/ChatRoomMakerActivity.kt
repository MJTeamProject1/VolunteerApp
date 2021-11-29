package com.team1.volunteerapp.Chat.ChatRoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_chat_room_maker.fab
import kotlinx.android.synthetic.main.content_chatroom_maker.*

class ChatRoomMakerActivity : AppCompatActivity() {
    private var radioSelect = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_maker)
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        setSupportActionBar(bottomAppBar)


        // 이미지 설정
        radioGroupVol.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio1 -> radioSelect = 1
                R.id.radio2 -> radioSelect = 2
                R.id.radio3 -> radioSelect = 3
                R.id.radio4 -> radioSelect = 4
            }
        }

        val addBtn = findViewById<FloatingActionButton>(R.id.fab)
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
                        1,
                        title,
                        content,
                        FBAuth.getUid(),
                        FBAuth.getUserData(4))
                )
            )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> { // 홈으로 돌아가기
                fab.hide(AnimationB.addVisibilityChanged)
                Handler().postDelayed({
                    finish()
                }, 300)
            }
        }
        return true
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