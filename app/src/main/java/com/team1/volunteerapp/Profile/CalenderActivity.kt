package com.team1.volunteerapp.Profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R
import java.io.FileInputStream
import java.io.FileOutputStream

class CalenderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        val calendarview = findViewById<CalendarView>(R.id.calenderview)
        val diaryview = findViewById<RecyclerView>(R.id.diaryContent)

        val items = mutableListOf<CalendarModel>()

        for(i in 0..5){
            items.add(CalendarModel("title${i}", "start${i}", "end${i}"))
        }

        //RecyclerView Adapter 연결
        val cal_rv = findViewById<RecyclerView>(R.id.diaryContent)
        val cal_rvAdapter = CalendarRVAdapter(items)

        cal_rv.adapter = cal_rvAdapter
        cal_rv.layoutManager = LinearLayoutManager(this)
        
        calendarview.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Log.d("calendar", "바뀜")
            diaryview.visibility = View.VISIBLE
        }

    }
}
