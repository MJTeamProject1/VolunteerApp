package com.team1.volunteerapp.Profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R
import java.io.FileInputStream
import java.io.FileOutputStream

class CalenderActivity : AppCompatActivity() {
    private var userdate :String? = null
    private var title :String? = null
    private var times :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        var count = 0
        val calendarview = findViewById<CalendarView>(R.id.calenderview)
        val infoview = findViewById<RelativeLayout>(R.id.infoview)
        val applytitle = findViewById<TextView>(R.id.mRV_itemText_title)
        val applystart = findViewById<TextView>(R.id.mRV_itemText_start)
        val applyend = findViewById<TextView>(R.id.mRV_itemText_end)
        val nowdate = findViewById<TextView>(R.id.calendardate)

        userdate = intent.getStringExtra("user_date")
        title = intent.getStringExtra("date_title")
        times = intent.getStringExtra("applywhen")


        var titlearr = title.toString().split("@")
        val datearry = userdate.toString().split("@")
        val timesarr = times.toString().split("@")

        calendarview.setOnDateChangeListener { view, year, month, dayOfMonth ->
            count = 0
            Log.d("calendar", "바뀜${year}년${month}월${dayOfMonth}일")
            nowdate.setText("${year}년 ${month + 1}월 ${dayOfMonth}일")
            if(userdate != ""){
                for(i in datearry){
                    if(dayOfMonth < 10){
                        if((i.substring(0,4) == year.toString()) && (i.substring(4,6) == (month + 1).toString()) && (i.substring(6,8) == "0${dayOfMonth}")){
                            infoview.visibility = View.VISIBLE
                            applytitle.text = titlearr[count]
                            applystart.text = timesarr[count].substring(0,2)
                            applyend.text = timesarr[count].subSequence(2,4)
                            break
                        }
                        else{
                            infoview.visibility = View.INVISIBLE
                        }
                    }
                    else{
                        if((i.substring(0,4) == year.toString()) && (i.substring(4,6) == (month + 1).toString()) && (i.substring(6,8) == dayOfMonth.toString())){
                            infoview.visibility = View.VISIBLE
                            applytitle.text = titlearr[count]
                            applystart.text = timesarr[count].substring(0,2)
                            applyend.text = timesarr[count].substring(2,4)
                            break
                        }
                        else{
                            infoview.visibility = View.INVISIBLE
                        }
                    }
                    count++
                }
            }
            //TODO 날짜가 일치한다면 리스트에 추가 후 보여주기
        }

    }
}
