package com.team1.volunteerapp.Chat.ChatRoom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_community2.bottomAppBar
import kotlinx.android.synthetic.main.activity_community2.fab
import java.lang.Exception

class ChatRoomListActivity : AppCompatActivity() {
    val chatRoomList_items = mutableListOf<ChatRoomInfoModel>()
    val chatRoomList_Key = mutableListOf<String>()
    lateinit var chatRoomList_rvAdapter : ChatRoomListRVAdapter
    private val uidKey = FBAuth.getUid()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_list)
        setSupportActionBar(bottomAppBar)

        // 하단 바
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()

        // RecyclerView 연결
        val chatRoomList_rv = findViewById<RecyclerView>(R.id.mRecyclerViewChatRoomList)
        chatRoomList_rvAdapter = ChatRoomListRVAdapter(chatRoomList_items)
        chatRoomList_rv.adapter = chatRoomList_rvAdapter
        chatRoomList_rv.layoutManager = LinearLayoutManager(this)

        // 데이터 가져오기
        getChatRoomListData()

        val addBtn = findViewById<FloatingActionButton>(R.id.fab)
        addBtn.setOnClickListener {
            fab.hide(AnimationB.addVisibilityChanged)
            val Intent = Intent(this, ChatRoomMakerActivity::class.java)
            Handler().postDelayed({
                startActivity(Intent)
            }, 150)
            chatRoomList_rvAdapter.notifyDataSetChanged()
        }

        chatRoomList_rvAdapter.itemClick = object : ChatRoomListRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                fab.hide(AnimationB.addVisibilityChanged)
                val intent = Intent(chatRoomList_rv.context, ChatRoomInfoActivity::class.java)
                intent.putExtra("roomKey", chatRoomList_Key[position].toString())
                Handler().postDelayed({
                    ContextCompat.startActivity(chatRoomList_rv.context, intent, null)
                },150)
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> { // 홈으로 돌아가기
                fab.hide(AnimationB.addVisibilityChanged)
                Handler().postDelayed({
                    finish()
                }, 300)
            }
            R.id.app_bar_community_list ->{

            }
        }
        return true
    }

    override fun onStart() {
        // 애니메이션 작동
        super.onStart()
        Handler().postDelayed({
            fab.show()
        }, 450)
    }
}