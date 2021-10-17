package com.team1.volunteerapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // RecyclerView 임시 테스트 데이터 삽입
        val items = mutableListOf<String>()
        items.add("Test Input 1")
        items.add("Test Input 2")
        items.add("Test Input 3")
        items.add("Test Input 4")
        items.add("Test Input 5")
        items.add("Test Input 6")
        items.add("Test Input 7")

        //RecyclerView Adapter 연결
        val profile_rv = findViewById<RecyclerView>(R.id.MyVolunListView)
        val profile_rvAdapter = ProfileRVAdapter(items)

        profile_rv.adapter = profile_rvAdapter
        profile_rv.layoutManager = LinearLayoutManager(this)

        // RecyclerView item을 클릭 시
//        profile_rvAdapter.itemClick= object : HomeRVAdapter.ItemClick{
//            override fun onClick(view: View, position: Int) {
//                Toast.makeText(baseContext, items[position], Toast.LENGTH_LONG).show()
//            }
//
//        }

    }

}