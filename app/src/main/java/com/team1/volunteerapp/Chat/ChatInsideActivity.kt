package com.team1.volunteerapp.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatInsideActivity : AppCompatActivity() {
    val chatData = mutableListOf<ChatModel>()

    //메세지를 보낸 시간
    val time = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
    val curTime = dateFormat.format(Date(time)).toString()

    lateinit var chat_rvAdapter : ChatInsideRVAdater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_inside)




        // RecyclerView 연결
        val chat_rv = findViewById<RecyclerView>(R.id.mRecyclerViewChat)
        chat_rvAdapter = ChatInsideRVAdater(chatData)
        chat_rv.adapter = chat_rvAdapter
        chat_rv.layoutManager = LinearLayoutManager(this)

        // 그룹 고유 키 값 가져오기
        val roomKey = intent.getStringExtra("roomKey")

        // 채팅 불러오기
        if (roomKey != null) {
            readChat(roomKey)
        }

        // 채팅 입력 시
        val inputBtn = findViewById<Button>(R.id.chatInputBtn)
        val inputText = findViewById<EditText>(R.id.chatEdit)
        inputBtn.setOnClickListener {
            Handler().postDelayed({
                if(inputText.text.toString() == null){
                    Toast.makeText(this, "채팅을 입력해주세요",Toast.LENGTH_SHORT).show()
                }
                else{
                    insertChat(roomKey, inputText.text.toString())
                }
            },100)
        }
    }


    private fun insertChat(roomKey: String?, inputText : String){
        val inputText2 = findViewById<EditText>(R.id.chatEdit)
        if (roomKey != null) {
            FBRef.chatRef
                .child(roomKey)
                .push()
                .setValue(
                    ChatModel(
                        FBAuth.getUserData(4),
                        FBAuth.getUid(),
                        inputText,
                        curTime
                    )
                )
        }

        inputText2.setText("")
    }

    private fun readChat(roomKey: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatData.clear()
                for(dataModel in snapshot.children){
                    try {
                        val item = dataModel.getValue(ChatModel::class.java)
                        if (item != null) {
                            Log.d("chatchatchatchat", item.text)
                        }
                        if (item != null) {
                            chatData.add(item)
                        }

                    }catch (e : Exception){
                        Log.d("chatchat", e.toString())
                    }
                }
                chat_rvAdapter.notifyDataSetChanged()

                //메시지를 보낼 시 화면을 맨 밑으로 내림
                val chat_rv = findViewById<RecyclerView>(R.id.mRecyclerViewChat)
                chat_rv?.scrollToPosition(chatData.size - 1)
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        FBRef.chatRef.child(roomKey).addValueEventListener(postListener)

    }
}