package com.team1.volunteerapp.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Chat.ChatRoom.ChatRoomListModel
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatInsideActivity : AppCompatActivity() {

    lateinit var databaseRef : DatabaseReference
    lateinit var valueEvent : ValueEventListener

    val chatData = mutableListOf<ChatModel>()
    val readUser = mutableListOf<ChatReadModel>()
    val chatCount = mutableListOf<Int>()
    //메세지를 보낸 시간
    val time = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
    val curTime = dateFormat.format(Date(time)).toString()

    private lateinit var auth: FirebaseAuth

    //다른 유저들 정보
    val userUidlist = mutableListOf<String>()

    lateinit var chat_rvAdapter : ChatInsideRVAdater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_inside)

        auth = Firebase.auth

        // 그룹 고유 키 값 가져오기
        val roomKey = intent.getStringExtra("roomKey")
        val usercount = intent.getIntExtra("count", 0)
        for(i in 0 until usercount){
            userUidlist.add(intent.getStringExtra("${i}useruid")!!)
        }



        // RecyclerView 연결
        val chat_rv = findViewById<RecyclerView>(R.id.mRecyclerViewChat)
        chat_rvAdapter = ChatInsideRVAdater(chatData,roomKey,chatCount)
        chat_rv.adapter = chat_rvAdapter
        chat_rv.layoutManager = LinearLayoutManager(this)


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
        val backBtn = findViewById<ImageView>(R.id.chatBack)
        backBtn.setOnClickListener {
            databaseRef.removeEventListener(valueEvent)
            finish()
        }
    }

    // 채팅 입력
    private fun insertChat(roomKey: String?, inputText : String){
        var fcmpush = FCMPush()
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
            //푸시를 받을 유저의 UID가 담긴 destinationUid 값을 넣어준후 fcmPush클래스의 sendMessage 메소드 호출
            for(uid in userUidlist){
                if(uid == auth.uid.toString()){
                    continue
                }
                else{
                    fcmpush.sendMessage("${uid}", "${FBAuth.getUserData(4)}", "${inputText}")
                }

            }
        }

        inputText2.setText("")
        chat_rvAdapter.notifyDataSetChanged()
    }

//    private fun readCharUser(roomKey: String){
//        FBRef.chatRef
//            .child(roomKey)
//            .child()
//    }

    // 채팅 읽기
    private fun readChat(roomKey: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatData.clear()

                for(dataModel in snapshot.children){
                    try {
                        val item = dataModel.getValue(ChatModel::class.java)
                        val item2 = dataModel.key

                        if (item != null) {
                            Log.d("chatchatchatchat", item.text)
                        }
                        if (item != null) {
                            chatData.add(item)
                        }

                        // 읽은 사람 기록
                        val post = ChatReadModel(FBAuth.getUid(), true)
                        val postValue = post.toMap()

                        val childUpdate = hashMapOf<String,Any?>(
                            "readUser/${FBAuth.getUid()}" to postValue
                        )
                        if (item2 != null) {
                            FBRef.chatRef.child(roomKey).child(item2).updateChildren(childUpdate)
                        }


                        // 채팅 읽은 사람 수
                        val item4 = dataModel.child("readUser").childrenCount
                        chatCount.add(item4.toInt())
                        Log.d("chatchatchatchatchatchatchatchat", item4.toString())

                        val item33 = dataModel.getValue(ChatRoomListModel::class.java)
                        val item44 = item33?.chatRoomMaxUnit
                        Log.d("outoutoutout", item44.toString())
                        // 채팅 읽은 사람 수 업데이트
                        val childUpdate2 = hashMapOf<String,Any>(
                            "readusercount" to item4.toInt()
                        )
                        if (item2 != null) {
                            FBRef.chatRef.child(roomKey).child(item2).updateChildren(childUpdate2)
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

        val postListener2 = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        FBRef.chatRoomListRef.child(roomKey).addValueEventListener(postListener2)
        // 뒤로가기 눌렀을 때 valueEvent 제거
        databaseRef = FBRef.chatRef.child(roomKey)
        valueEvent = databaseRef.addValueEventListener(postListener)

    }
    // 뒤로가기 눌렀을 때 valueEvent 제거
    override fun onBackPressed() {
        databaseRef.removeEventListener(valueEvent)
        finish()
    }
}