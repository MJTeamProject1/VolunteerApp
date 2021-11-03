package com.team1.volunteerapp.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.team1.volunteerapp.HomeActivity
import com.team1.volunteerapp.R

class CommActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var sido : String? = null
    var gugun : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")

        val CommList = arrayListOf(
            CUser("그룹1", 1, "안녕하세요"),
            CUser("그룹2", 2, "안녕하세요"),
            CUser("그룹3", 3, "안녕하세요"),
            CUser("그룹4", 4, "안녕하세요"),
            CUser("그룹5", 5, "안녕하세요")
        )
        val Comm_rv = findViewById<RecyclerView>(R.id.rvComm)
        val Comm_rvAdapter = CommAdapter(CommList)

        Comm_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        Comm_rv.setHasFixedSize(true)
        Comm_rv.adapter = Comm_rvAdapter

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {// 네비게이션 뷰 아이템 클릭시
        val intentc = Intent(this, CommActivity::class.java)
        val intentr = Intent(this, ReviewActivity::class.java)

        when(item.itemId)
        {
            R.id.community -> startActivity(intentc)
            R.id.review -> startActivity(intentr)
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
}