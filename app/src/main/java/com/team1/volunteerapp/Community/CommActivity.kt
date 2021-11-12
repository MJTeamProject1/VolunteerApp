package com.team1.volunteerapp.Community

import android.annotation.SuppressLint
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.team1.volunteerapp.Favorite.FavoriteRVAdapter
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_home.*

class CommActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener =
        object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onShown(fab: FloatingActionButton?) {
                super.onShown(fab)
            }

            @SuppressLint("NewApi")
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
//                fab?.show()
            }
        }

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()
    private lateinit var commRVAdapter: CommAdapter
    var setDrawr = false
    var nickname : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        setSupportActionBar(bottomAppBar)

        //putExtra 데이터 받기
        nickname = intent.getStringExtra("nickname")

        println("~~~~~~~~~~~~~~~~~~~~" + nickname)

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
                intent.putExtra("review",false)
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
            fab.hide(addVisibilityChanged)
            Handler().postDelayed({
                val intent = Intent(this, BoardWriteActivity::class.java)
                intent.putExtra("nickname", nickname)
                startActivity(intent)
            }, 300)

        }
    }

    private fun getFBBoardData(){
        val postListener = object : ValueEventListener{
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
        FBRef.communityRef.addValueEventListener(postListener)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 뷰 아이템 클릭시
        val intentr = Intent(this, ReviewActivity::class.java)

        when(item.itemId)
        {
            R.id.community -> {
                Toast.makeText(this,"이미 커뮤니티 입니다.", Toast.LENGTH_LONG).show()
            }
            R.id.review -> {
                fab.hide(addVisibilityChanged)
                Handler().postDelayed({
                    intentr.putExtra("nickname", nickname)
                    startActivity(intentr)
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
                fab.hide(addVisibilityChanged)
                Handler().postDelayed({
                    finish()
                }, 300)
            }
            R.id.app_bar_community_list ->{
                val layoutDrawerComm = findViewById<DrawerLayout>(R.id.layout_drawer_comm)
                if(setDrawr){
                    layoutDrawerComm.closeDrawer(GravityCompat.START)
                    setDrawr = false
                }else{
                    layoutDrawerComm.openDrawer(GravityCompat.START)
                    setDrawr = true
                }
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
