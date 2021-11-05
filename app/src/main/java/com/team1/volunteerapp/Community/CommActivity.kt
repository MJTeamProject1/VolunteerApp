package com.team1.volunteerapp.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.team1.volunteerapp.Favorite.FavoriteRVAdapter
import com.team1.volunteerapp.HomeActivity
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef

class CommActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var sido : String? = null
    var gugun : String? = null
//    private lateinit var userRecyclerView: RecyclerView


    val userArrayList = arrayListOf<CUser>()
    lateinit var Comm_rvAdapter : CommAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

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



        val btnNaviComm = findViewById<ImageView>(R.id.btnNaviComm)
        val layoutDrawerComm = findViewById<DrawerLayout>(R.id.layout_drawer_comm)
        btnNaviComm.setOnClickListener {
            layoutDrawerComm.openDrawer(GravityCompat.START)
        }

        val naviViewComm = findViewById<NavigationView>(R.id.naviViewComm)
        naviViewComm.setNavigationItemSelectedListener(this)

        val homeCommButton = findViewById<ImageButton>(R.id.homeButtonComm)
        homeCommButton.setOnClickListener { // 홈으로 돌아가기
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("sido",sido)
            intent.putExtra("gugun",gugun)
            finishAffinity()
            startActivity(intent)
            finish()
        }

        val btnWriteComm = findViewById<Button>(R.id.btnWriteComm)
        btnWriteComm.setOnClickListener {
            val intent = Intent(this, WriteCommActivity::class.java)
            startActivity(intent)
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
        val intentc = Intent(this, CommActivity::class.java)
        val intentr = Intent(this, ReviewActivity::class.java)

        when(item.itemId)
        {
            R.id.community -> startActivity(intentc)
            R.id.review -> startActivity(intentr)
        }
        finish()

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
}


