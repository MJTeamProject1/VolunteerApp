package com.team1.volunteerapp.Community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.HomeActivity
import com.team1.volunteerapp.R

class ReviewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var sido : String? = null
    var gugun : String? = null

    val userArrayList = arrayListOf<CUser>()
    lateinit var Rev_rvAdapter : CommAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

//        val CommList = arrayListOf(
//            CUser("제목1",  "이름1", "내용1"),
//            CUser("제목2",  "이름2", "내용1"),
//            CUser("제목3",  "이름3", "내용1"),
//            CUser("제목4",  "이름4", "내용1"),
//            CUser("제목5",  "이름5", "내용1")
//        )
        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")

        val Rev_rv = findViewById<RecyclerView>(R.id.rvReview)
        Rev_rvAdapter = CommAdapter(userArrayList)
        Rev_rv.adapter = Rev_rvAdapter

        Rev_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        Rev_rv.setHasFixedSize(true)

        getUserData()

        val btnNaviReview = findViewById<ImageView>(R.id.btnNaviRev)
        val layoutDrawerRev = findViewById<DrawerLayout>(R.id.layout_drawer_review)
        btnNaviReview.setOnClickListener {
            layoutDrawerRev.openDrawer(GravityCompat.START)
        }

        val naviViewRev = findViewById<NavigationView>(R.id.naviViewRev)
        naviViewRev.setNavigationItemSelectedListener(this)

        val homeRevButton = findViewById<ImageButton>(R.id.homeButtonRev)
        homeRevButton.setOnClickListener { // 홈으로 돌아가기
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val btnWriteReview = findViewById<Button>(R.id.btnWriteReview)
        btnWriteReview.setOnClickListener {
            val intent = Intent(this, WriteReviewActivity::class.java)
            startActivity(intent)
        }

    }


    var dbref = FirebaseDatabase.getInstance().getReference("Review_list")

    private fun getUserData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(dataModel in snapshot.children){
                    Log.d("cascasdcasac",dataModel.toString())
                    userArrayList.add(dataModel.getValue(CUser::class.java)!!)
                }
                Rev_rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        //key 값만 가져옴
        dbref.addValueEventListener(postListener)
        //추가
        Rev_rvAdapter.setonItemClickListener(object : CommAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
//                Toast.makeText(this@CommActivity,"2",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ReviewActivity, InfoActivity2::class.java)
                intent.putExtra("maketitler", userArrayList[position].Title)
                intent.putExtra("makenickr", userArrayList[position].Nickname)
                intent.putExtra("makecontr", userArrayList[position].Contents)
                startActivity(intent)

            }
        })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 뷰 아이템 클릭시
        val intentc = Intent(this, CommActivity::class.java)
        val intentr = Intent(this, ReviewActivity::class.java)

        when(item.itemId)
        {
            R.id.community -> startActivity(intentc)
            R.id.review -> startActivity(intentr)
        }
        finish()

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
}