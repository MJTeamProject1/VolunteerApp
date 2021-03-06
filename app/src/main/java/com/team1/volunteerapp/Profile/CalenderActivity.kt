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
    private var mBackWait:Long = 0
    lateinit var calendarview : MCalendarView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)


        var count = 0
        calendarview = findViewById<MCalendarView>(R.id.calenderview)
        val infoview = findViewById<RelativeLayout>(R.id.infoview)
        val applytitle = findViewById<TextView>(R.id.mRV_itemText_title)
        val applystart = findViewById<TextView>(R.id.mRV_itemText_start)
        val applyend = findViewById<TextView>(R.id.mRV_itemText_end)
        val nowdate = findViewById<TextView>(R.id.calendardate)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy??? MM??? dd???")
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

        if(userdate != "" && userdate != null){
            for(i in datearry){
                calendarview.unMarkDate(DateData(i.substring(0,4).toInt(), i.substring(4,6).toInt(),i.substring(6,8).toInt()))
            }
        }
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
        //????????? ?????? ?????? ??????
        if(userdate != "" && userdate != null){
            for(i in datearry){
                if((i.substring(0,4) == nowdatearr[0].toString()) && (i.substring(4,6) == nowdatearr[1].toString()) && (i.substring(6,8) == nowdatearr[2].toString())){
                    //?????? ?????? ????????? ?????? ????????? ????????? ????????? skip
                    continue
                }
                else{
                    calendarview.markDate(DateData(i.substring(0,4).toInt(), i.substring(4,6).toInt(),i.substring(6,8).toInt()).setMarkStyle(
                        MarkStyle(MarkStyle.DOT, Color.rgb(246,145,16))
                    ))
                }
            }
        }
        //?????? ?????? ???
        calendarview.markDate(DateData(nowdatearr[0].toInt(), nowdatearr[1].toInt(), nowdatearr[2].toInt()).setMarkStyle(
            MarkStyle(MarkStyle.BACKGROUND, Color.rgb(246,145,16))
        ))

        calendarview.setOnDateClickListener(object :OnDateClickListener(){
            override fun onDateClick(view: View, date: DateData){
                //?????? ??? ????????? ????????? ????????? ?????? ??????
                if(oldyear != null){
                    calendarview.unMarkDate(oldyear!!, oldmonth!!, oldyday!!)
                    //?????? ?????? ???????????? ????????? ?????? ????????? ?????? ??????
                    if(userdate != "" && userdate != null){
                        for(i in datearry){
                            //????????? ?????? ?????? ??????
                            if((i.substring(0,4).toInt() == oldyear) && (i.substring(4,6).toInt() == oldmonth) && (i.substring(6,8).toInt() == oldyday)){
                                calendarview.markDate(DateData(i.substring(0,4).toInt(), i.substring(4,6).toInt(),i.substring(6,8).toInt()).setMarkStyle(
                                    MarkStyle(MarkStyle.DOT, Color.rgb(246,145,16))
                                ))
                            }
                        }
                    }
                }

                //?????? ????????? ????????? ???????????? ?????? ???????????? ????????? ???????????? ?????????
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

                //?????? ?????????
                if(flags){
                    calendarview.unMarkDate(date.year, date.month, date.day)
                    //?????? ?????? ???????????????
                    calendarview.markDate(DateData(date.year, date.month, date.day).setMarkStyle(
                        MarkStyle(MarkStyle.BACKGROUND, Color.rgb(255,188,26))
                    ))
                }
                //???????????? ?????? ??????
                else{
                    calendarview.markDate(DateData(date.year, date.month, date.day).setMarkStyle(
                        MarkStyle(MarkStyle.BACKGROUND, Color.rgb(255,188,26))
                    ))
                }
                oldyear = date.year
                oldyday = date.day
                oldmonth = date.month
                count = 0
                Log.d("calendar", "??????${date.year}???${date.month}???${date.day}???")
                nowdate.setText("${date.year}??? ${date.month}??? ${date.day}???")
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

                //?????? ?????? ???????????? ?????? ??????
                calendarview.unMarkDate(nowdatearr[0].toInt(), nowdatearr[1].toInt(), nowdatearr[2].toInt())
                calendarview.markDate(DateData(nowdatearr[0].toInt(), nowdatearr[1].toInt(), nowdatearr[2].toInt()).setMarkStyle(
                    MarkStyle(MarkStyle.BACKGROUND, Color.rgb(246,145,16))
                ))
            }
        })

    }

    override fun onBackPressed() {
        calendarview.unMarkDate(oldyear!!, oldmonth!!, oldyday!!)
        finish()
    }
}
