package com.team1.volunteerapp.Community

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Auth.IntroActivity
import com.team1.volunteerapp.Favorite.FavoriteRVAdapter
import com.team1.volunteerapp.HomeActivity
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
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
    var sido : String? = null
    var gugun : String? = null
    var setDrawr = false
    val userArrayList = arrayListOf<CUser>()
    lateinit var Comm_rvAdapter : CommAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        setSupportActionBar(bottomAppBar)

        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")

        //Recycler view 연결
        val Comm_rv = findViewById<RecyclerView>(R.id.rvComm2)
        Comm_rvAdapter = CommAdapter(userArrayList)
        Comm_rv.adapter = Comm_rvAdapter

        Comm_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        Comm_rv.setHasFixedSize(true)

        // DB에서 데이터 받아오기기
       getUserData()

//        val btnNaviComm = findViewById<ImageView>(R.id.btnNaviComm)
//        val layoutDrawerComm = findViewById<DrawerLayout>(R.id.layout_drawer_comm)
//        btnNaviComm.setOnClickListener {
//            layoutDrawerComm.openDrawer(GravityCompat.START)
//        }

        val naviViewComm = findViewById<NavigationView>(R.id.naviViewComm)
        naviViewComm.setNavigationItemSelectedListener(this)

        // 글 쓰기 버튼
        val btnWriteComm = findViewById<FloatingActionButton>(R.id.fab)
        btnWriteComm.setOnClickListener {
            fab.hide(addVisibilityChanged)
            Handler().postDelayed({
                val intent = Intent(this, WriteCommActivity::class.java)
                startActivity(intent)
            }, 300)

        }
    }

    var dbref = FirebaseDatabase.getInstance().getReference("Community_list")

    private fun getUserData(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(dataModel in snapshot.children){
                    Log.d("cascascasac",dataModel.toString())
                    userArrayList.add(dataModel.getValue(CUser::class.java)!!)
                }
                Comm_rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        //key 값만 가져옴
        dbref.addValueEventListener(postListener)
        //추가
        Comm_rvAdapter.setonItemClickListener(object : CommAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
//                Toast.makeText(this@CommActivity,"2",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@CommActivity, InfoActivity::class.java)
                intent.putExtra("maketitle", userArrayList[position].Title)
                intent.putExtra("makenick", userArrayList[position].Nickname)
                intent.putExtra("makecont", userArrayList[position].Contents)
                startActivity(intent)

            }

        })


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



// 네비게이션
//val layoutDrawerComm = findViewById<DrawerLayout>(R.id.layout_drawer_comm)
//layoutDrawerComm.openDrawer(GravityCompat.START)


