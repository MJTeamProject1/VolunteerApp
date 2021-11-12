package com.team1.volunteerapp.Community

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.HomeActivity
import com.team1.volunteerapp.R
import kotlinx.android.synthetic.main.activity_home.*

class ReviewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
    lateinit var Rev_rvAdapter : CommAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setSupportActionBar(bottomAppBar)

//        val CommList = arrayListOf(
//            CUser("제목1",  "이름1", "내용1"),
//            CUser("제목2",  "이름2", "내용1"),
//            CUser("제목3",  "이름3", "내용1"),
//            CUser("제목4",  "이름4", "내용1"),
//            CUser("제목5",  "이름5", "내용1")
//        )
        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")

//        val Rev_rv = findViewById<RecyclerView>(R.id.rvReview)
//        Rev_rvAdapter = CommAdapter(userArrayList)
//        Rev_rv.adapter = Rev_rvAdapter
//
//        Rev_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        Rev_rv.setHasFixedSize(true)
//
//        getUserData()

        val naviViewRev = findViewById<NavigationView>(R.id.naviViewRev)
        naviViewRev.setNavigationItemSelectedListener(this)

        // 글쓰기 부분
        val btnWriteReview = findViewById<FloatingActionButton>(R.id.fab)
        btnWriteReview.setOnClickListener {
            fab.hide(addVisibilityChanged)
            Handler().postDelayed({
                val intent = Intent(this, WriteReviewActivity::class.java)
                startActivity(intent)
            }, 300)

        }

    }


    var dbref = FirebaseDatabase.getInstance().getReference("Review_list")

//    private fun getUserData(){
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                for(dataModel in snapshot.children){
//                    Log.d("cascasdcasac",dataModel.toString())
//                    userArrayList.add(dataModel.getValue(CUser::class.java)!!)
//                }
//                Rev_rvAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        }
//        //key 값만 가져옴
//        dbref.addValueEventListener(postListener)
//        //추가
//        Rev_rvAdapter.setonItemClickListener(object : CommAdapter.onItemClickListener{
//            override fun onItemClick(position: Int) {
////                Toast.makeText(this@CommActivity,"2",Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@ReviewActivity, InfoActivity2::class.java)
//                intent.putExtra("maketitler", userArrayList[position].Title)
//                intent.putExtra("makenickr", userArrayList[position].Nickname)
//                intent.putExtra("makecontr", userArrayList[position].Contents)
//                startActivity(intent)
//
//            }
//        })
//    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 뷰 아이템 클릭시
        val intentc = Intent(this, CommActivity::class.java)

        when(item.itemId)
        {
            R.id.community -> {
                fab.hide(addVisibilityChanged)
                Handler().postDelayed({
                    startActivity(intentc)
                    finish()
                }, 150)
            }
            R.id.review -> {
                Toast.makeText(this,"이미 후기 입니다.", Toast.LENGTH_LONG).show()
            }
        }

        val layoutDrawerRev = findViewById<DrawerLayout>(R.id.layout_drawer_review)
        layoutDrawerRev.closeDrawers() //네비게이션 뷰 닫기
        return false
    }

    override fun onBackPressed() {


        val layoutDrawerRev = findViewById<DrawerLayout>(R.id.layout_drawer_review)
        if(layoutDrawerRev.isDrawerOpen(GravityCompat.START)){
            layoutDrawerRev.closeDrawers()
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
                val layoutDrawerComm = findViewById<DrawerLayout>(R.id.layout_drawer_review)
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