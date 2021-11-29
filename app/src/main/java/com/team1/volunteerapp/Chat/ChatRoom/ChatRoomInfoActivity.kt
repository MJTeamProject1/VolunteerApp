package com.team1.volunteerapp.Chat.ChatRoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.Chat.ChatInsideActivity
import com.team1.volunteerapp.Chat.ChatJoinUserModel
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_chat_room_info.fab

class ChatRoomInfoActivity : AppCompatActivity() {
    val userData = mutableListOf<ChatJoinUserModel>()
    val userDataCheck = mutableListOf<String>()
    var updateGroupCount = 0
    private var roomKey = ""
    lateinit var chatUserJoinInfo_rvAdapter : ChatRoomInfoRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_info)
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        setSupportActionBar(bottomAppBar)

        // 하단 바
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()

        // 그룹 고유 키 값 가져오기
        roomKey = intent.getStringExtra("roomKey").toString()

        //Recycler view 연결
        val chatRoomInfo_rv = findViewById<RecyclerView>(R.id.chatRoomInfoJoinRV)
        chatUserJoinInfo_rvAdapter = ChatRoomInfoRVAdapter(userData)
        chatRoomInfo_rv.adapter = chatUserJoinInfo_rvAdapter
        chatRoomInfo_rv.layoutManager = GridLayoutManager(this,2)

        // 그룹 정보 가져오기
        getGroupInfo(roomKey!!)


        // 그룹 채팅 입장 버튼
        val groupChatBtn = findViewById<FloatingActionButton>(R.id.fab)
        groupChatBtn.setOnClickListener {
            if(userDataCheck.contains(FBAuth.getUid())){
                val intent  = Intent(this, ChatInsideActivity::class.java)
                intent.putExtra("roomKey", roomKey)
                startActivity(intent)
            }else{
                Toast.makeText(this,"그룹 가입을 먼저 해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 그룹 정보 가져오기
    private fun getGroupInfo(roomKey : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(ChatRoomInfoModel::class.java)

                val title = findViewById<TextView>(R.id.ChatRoomTitle)
                val content = findViewById<TextView>(R.id.ChatRoomSubTitle)
                val groupCountInfo = findViewById<TextView>(R.id.groupCountInfo)
                val groupMaster = findViewById<TextView>(R.id.groupMaster)
                val chatImg = findViewById<ImageView>(R.id.chatRoomInfoImg)

                title.text = dataModel?.chatInfo?.chatRoomTitle
                content.text = dataModel?.chatInfo?.chatRoomSubTitle
                var setGroupCount = dataModel?.chatInfo?.chatRoomMaxUnit?.minus(1)
                groupCountInfo.text = setGroupCount.toString()+"명"
                updateGroupCount = dataModel?.chatInfo?.chatRoomMaxUnit!!
                groupMaster.text = dataModel?.chatInfo?.chatRoomMakerNickName
                var imgNumber: Int = dataModel?.chatInfo?.chatRoomNumber
                when(imgNumber){
                    1-> chatImg.setImageResource(R.drawable.cooperation)
                    2-> chatImg.setImageResource(R.drawable.fist)
                    3-> chatImg.setImageResource(R.drawable.savenature)
                    4-> chatImg.setImageResource(R.drawable.dove)
                }


                userDataCheck.add(dataModel?.chatInfo.chatRoomMakerUid)

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

    // 그룹 가입
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
            Toast.makeText(this,"가입 완료!",Toast.LENGTH_SHORT).show()
            FBRef.chatRoomListRef.updateChildren(childUpdates)

        }else{
            Toast.makeText(this,"이미 가입된 그룹입니다.",Toast.LENGTH_SHORT).show()
        }

    }

    // 화면 아래 메뉴 바
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu_chatinfo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                fab.hide(AnimationB.addVisibilityChanged)
                Handler().postDelayed({
                    finish()
                }, 300)
            }

            // 그룹 가입 버튼 눌렀을 시
            R.id.app_bar_join -> {
                joinGroupUserData(roomKey!!)
            }
        }
        return true
    }

}