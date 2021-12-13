package com.team1.volunteerapp.Profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R
import sun.bob.mcalendarview.MCalendarView
import sun.bob.mcalendarview.MarkStyle
import sun.bob.mcalendarview.listeners.OnDateClickListener
import sun.bob.mcalendarview.views.BaseCellView
import sun.bob.mcalendarview.vo.DateData
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.graphics.ColorFilter
import android.graphics.Paint


class CalenderActivity : AppCompatActivity() {
    private var userdate :String? = null
    private var title :String? = null
    private var times :String? = null

    private var oldyear: Int? = null
    private var oldmonth: Int? = null
    private var oldyday: Int? = null

    private var flags : Boolean = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)


        var count = 0
        val calendarview = findViewById<MCalendarView>(R.id.calenderview)
        val infoview = findViewById<RelativeLayout>(R.id.infoview)
        val applytitle = findViewById<TextView>(R.id.mRV_itemText_title)
        val applystart = findViewById<TextView>(R.id.mRV_itemText_start)
        val applyend = findViewById<TextView>(R.id.mRV_itemText_end)
        val nowdate = findViewById<TextView>(R.id.calendardate)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val formatted = current.format(formatter)

        val current2 = LocalDateTime.now()
        val formatter2 = DateTimeFormatter.ISO_DATE
        val formatted2 = current.format(formatter2)
        val nowdatearr = formatted2.split("-")

        userdate = intent.getStringExtra("user_date")
        title = intent.getStringExtra("date_title")
        times = intent.getStringExtra("applywhen")


        var titlearr = title.toString().split("@")
        val datearry = userdate.toString().split("@")
        val timesarr = times.toString().split("@")


        nowdate.setText(formatted)

        if((userdate != "") && (userdate != null)){
            for(i in datearry){
                if((i.substring(0,4) == nowdatearr[0].toString()) && (i.substring(4,6) == nowdatearr[1].toString()) && (i.substring(6,8) == nowdatearr[2].toString())){
                    infoview.visibility = View.VISIBLE
                    applytitle.text = titlearr[count]
                    applystart.text = timesarr[count].substring(0,2)
                    applyend.text = timesarr[count].substring(2,4)
                    break
                }
                else{
                    infoview.visibility = View.INVISIBLE
                }
                count++
            }
        }
        //TODO 같은 날짜에 두 개 이상의 하이라이트 주기 불가능 그러므로 맞춰서 해야한다..
        //신청된 날짜 강조 표시
        if(userdate != "" && userdate != null){
            for(i in datearry){
                if((i.substring(0,4) == nowdatearr[0].toString()) && (i.substring(4,6) == nowdatearr[1].toString()) && (i.substring(6,8) == nowdatearr[2].toString())){
                    //만약 오늘 날짜에 강조 표시가 생겨야 한다면 skip
                    continue
                }
                else{
                    calendarview.markDate(DateData(i.substring(0,4).toInt(), i.substring(4,6).toInt(),i.substring(6,8).toInt()).setMarkStyle(
                        MarkStyle(MarkStyle.DOT, Color.rgb(246,145,16))
                    ))
                }
            }
        }
        //오늘 날짜 색
        calendarview.markDate(DateData(nowdatearr[0].toInt(), nowdatearr[1].toInt(), nowdatearr[2].toInt()).setMarkStyle(
            MarkStyle(MarkStyle.BACKGROUND, Color.rgb(246,145,16))
        ))

        calendarview.setOnDateClickListener(object :OnDateClickListener(){
            override fun onDateClick(view: View, date: DateData){
                //다른 거 클릭시 이전에 클릭된 색상 빼기
                if(oldyear != null){
                    calendarview.unMarkDate(oldyear!!, oldmonth!!, oldyday!!)
                    //만약 이미 디자인이 있었던 날짜 였다면 다시 추가
                    if(userdate != "" && userdate != null){
                        for(i in datearry){
                            //강조된 날짜 다시 표시
                            if((i.substring(0,4).toInt() == oldyear) && (i.substring(4,6).toInt() == oldmonth) && (i.substring(6,8).toInt() == oldyday)){
                                calendarview.markDate(DateData(i.substring(0,4).toInt(), i.substring(4,6).toInt(),i.substring(6,8).toInt()).setMarkStyle(
                                    MarkStyle(MarkStyle.DOT, Color.rgb(246,145,16))
                                ))
                            }
                        }
                    }
                }

                //만약 선택한 날짜에 디자인이 있는 날짜라면 지우고 다시쓰기 활성화
                if(userdate != "" && userdate != null){
                    for(i in datearry){
                        if((i.substring(0,4).toInt() == date.year) && (i.substring(4,6).toInt() == date.month) && (i.substring(6,8).toInt() == date.day)){
                            flags = true
                            break
                        }
                        else{
                            flags = false
                        }
                    }
                }

                //만약 있다면
                if(flags){
                    calendarview.unMarkDate(date.year, date.month, date.day)
                    //선택 날짜 애니메이션
                    calendarview.markDate(DateData(date.year, date.month, date.day).setMarkStyle(
                        MarkStyle(MarkStyle.BACKGROUND, Color.rgb(255,188,26))
                    ))
                }
                //아니라면 그냥 추가
                else{
                    calendarview.markDate(DateData(date.year, date.month, date.day).setMarkStyle(
                        MarkStyle(MarkStyle.BACKGROUND, Color.rgb(255,188,26))
                    ))
                }
                oldyear = date.year
                oldyday = date.day
                oldmonth = date.month
                count = 0
                Log.d("calendar", "바뀜${date.year}년${date.month}월${date.day}일")
                nowdate.setText("${date.year}년 ${date.month}월 ${date.day}일")
                if(userdate != "" && userdate != null){
                    for(i in datearry){
                        if(date.day < 10){
                            if((i.substring(0,4) == date.year.toString()) && (i.substring(4,6) == date.month.toString()) && (i.substring(6,8) == "0${date.day}")){
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
                            if((i.substring(0,4) == date.year.toString()) && (i.substring(4,6) == date.month.toString()) && (i.substring(6,8) == date.day.toString())){
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

                //오늘 날짜 계속하여 색상 표기
                calendarview.unMarkDate(nowdatearr[0].toInt(), nowdatearr[1].toInt(), nowdatearr[2].toInt())
                calendarview.markDate(DateData(nowdatearr[0].toInt(), nowdatearr[1].toInt(), nowdatearr[2].toInt()).setMarkStyle(
                    MarkStyle(MarkStyle.BACKGROUND, Color.rgb(246,145,16))
                ))
            }
        })

    }
}
