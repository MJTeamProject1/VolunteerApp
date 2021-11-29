package com.team1.volunteerapp.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.Chat.ChatRoom.ChatRoomListActivity
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_home.*

class ReviewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()
    private lateinit var commRVAdapter: CommAdapter
    private var setDrawr = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setSupportActionBar(bottomAppBar)

        // 하단 바
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()

        //Recycler view 연결
        val comm_rv = findViewById<RecyclerView>(R.id.rvComm2)

        commRVAdapter = CommAdapter(boardDataList)
        comm_rv.adapter = commRVAdapter
        comm_rv.layoutManager = LinearLayoutManager(this)

        commRVAdapter.itemClick = object : CommAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                //눌렀을때 어떻게 할지
                val intent = Intent(view.context,BoardInsideActivity::class.java)
                intent.putExtra("key",boardKeyList[position])
                intent.putExtra("review",true)
                startActivity(intent)
            }
        }

        // DB에서 데이터 받아오기기
        getFBBoardData()

        val naviViewComm = findViewById<NavigationView>(R.id.naviViewComm)
        naviViewComm.setNavigationItemSelectedListener(this)

        // 글 쓰기 버튼
        val btnWriteComm = findViewById<FloatingActionButton>(R.id.fab)
        btnWriteComm.setOnClickListener {
            fab.hide(AnimationB.addVisibilityChanged)
            Handler().postDelayed({
                val intent = Intent(this, BoardWriteActivity::class.java)
                startActivity(intent)
            }, 300)

        }
    }
    private fun getFBBoardData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                boardDataList.clear()

                for(dataModel in snapshot.children){
                    Log.d("asvv",dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }

                boardDataList.reverse()
                boardKeyList.reverse()
                commRVAdapter.notifyDataSetChanged()
                Log.d("asvv",boardDataList.toString())

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        FBRef.reviewRef.addValueEventListener(postListener)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 뷰 아이템 클릭시
        val intentr = Intent(this, CommActivity::class.java)
        val intentChat = Intent(this, ChatRoomListActivity::class.java)

        when(item.itemId)
        {
            R.id.community -> {
                fab.hide(AnimationB.addVisibilityChanged)
                Handler().postDelayed({
                    startActivity(intentr)
                    finish()
                }, 150)
            }
            R.id.review -> {
                Toast.makeText(this,"이미 후기 입니다.", Toast.LENGTH_LONG).show()
            }
            R.id.chat ->{
                fab.hide(AnimationB.addVisibilityChanged)
                Handler().postDelayed({
                    startActivity(intentChat)
                    finish()
                }, 150)
            }
        }
        val layoutDrawerComm = findViewById<DrawerLayout>(R.id.layout_drawer_comm)
        layoutDrawerComm.closeDrawers() //네비게이션 뷰 닫기
        return false
    }

    override fun onBackPressed() {

        val layoutDrawerComm = findViewById<DrawerLayout>(R.id.layout_drawer_comm)
        if(layoutDrawerComm.isDrawerOpen(GravityCompat.START)){
            layoutDrawerComm.closeDrawers()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> { // 홈으로 돌아가기
                val layoutDrawerComm = findViewById<DrawerLayout>(R.id.layout_drawer_comm)
                setDrawr = if(setDrawr){
                    layoutDrawerComm.closeDrawer(GravityCompat.START)
                    false
                }else{
                    layoutDrawerComm.openDrawer(GravityCompat.START)
                    true
                }
            }
            R.id.app_bar_community_list ->{
                fab.hide(AnimationB.addVisibilityChanged)
                Handler().postDelayed({
                    finish()
                }, 300)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu_community, menu)
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