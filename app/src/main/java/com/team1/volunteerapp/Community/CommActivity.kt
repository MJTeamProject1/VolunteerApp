package com.team1.volunteerapp.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class CommActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comm)

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
    }
}