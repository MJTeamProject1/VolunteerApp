package com.team1.volunteerapp.Profile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Auth.IntroActivity
import com.team1.volunteerapp.BottomNavDrawerFragment
import com.team1.volunteerapp.Community.CommActivity
import com.team1.volunteerapp.Favorite.FavoritesActivity
import com.team1.volunteerapp.HomeActivity
import com.team1.volunteerapp.R
import kotlinx.android.synthetic.main.activity_home.*

class ProfileActivity : AppCompatActivity() {
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
    private lateinit var pieChart: PieChart
    var voltime :String? = null
    var voltitle : String? = null
    var volgoaltime : String? = null
    var usernickname : String? = null
    var sido : String? = null
    var gugun : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(bottomAppBar)

        val homeButton = findViewById<FloatingActionButton>(R.id.fab)
        val profileInfo = findViewById<TextView>(R.id.profileInfo)

        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")

        if(intent.hasExtra("time") && intent.hasExtra("title")){
            voltime = intent.getStringExtra("time")
            voltitle = intent.getStringExtra("title")
        }
        else{
            voltime = ""
            voltitle = ""
        }
        volgoaltime = intent.getStringExtra("goaltime")
        usernickname = intent.getStringExtra("nickname")
        pieChart = findViewById(R.id.PieChartMyVolune)

        //닉네임 설정
        profileInfo.setText(usernickname)

        val items = mutableListOf<String>()

        initPieChart()
        setDataToPieChart()
        if(voltitle != ""){
            val volarray = voltitle.toString().split("@")
            for(i in volarray){
                items.add(i)
            }
        }


        //RecyclerView Adapter 연결
        val profile_rv = findViewById<RecyclerView>(R.id.mRecyclerViewProfile)
        val profile_rvAdapter = ProfileRVAdapter(items)

        profile_rv.adapter = profile_rvAdapter
        profile_rv.layoutManager = LinearLayoutManager(this)

        //구분선 넣기
        val dividerItemDecoration =
            DividerItemDecoration(profile_rv.context, LinearLayoutManager(this).orientation)

        profile_rv.addItemDecoration(dividerItemDecoration)

        homeButton.setOnClickListener { // 홈으로 돌아가기
            fab.hide(addVisibilityChanged)
            Handler().postDelayed({
//                val intent = Intent(this, HomeActivity::class.java)
//                intent.putExtra("sido",sido)
//                intent.putExtra("gugun",gugun)
//                finishAffinity()
//                startActivity(intent)
                finish()
            }, 300)
        }
    }


    //Pie Chart 생성
    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = "봉사시간 현황"
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.isRotationEnabled = false
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.isWordWrapEnabled = true

    }

    private fun setDataToPieChart() {
        var timepercent : Float = 0F
        var goalpercent : Float = 0F
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        if(voltime == ""){
            dataEntries.add(PieEntry(0f, "봉사시간"))
        }
        else{
            timepercent = (voltime.toString().toFloat() / volgoaltime.toString().toFloat() * 100)
            goalpercent = 100 - timepercent
            if(goalpercent < 0){
                timepercent = 100F
                goalpercent = 0F
            }
            dataEntries.add(PieEntry(timepercent, "봉사시간"))
        }
        dataEntries.add(PieEntry(goalpercent, "남은시간")) // 입력된 목표 봉사시간에서 -하기

        val colors: ArrayList<Int> = ArrayList() // 각 영역의 색상
        colors.add(Color.parseColor("#4DD0E1"))
        colors.add(Color.parseColor("#FFF176"))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // %로 설정
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //가운데 빈 공간
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)


        //빈 공간에 텍스트
        pieChart.setDrawCenterText(true);
        pieChart.centerText = "봉사시간 : ${voltime}시간"



        pieChart.invalidate()

    }

    //bottom bar

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("로그아웃")
                builder.setMessage("정말로 로그아웃 하시겠습니까?")
                builder.setNegativeButton("취소",
                    { dialogInterface: DialogInterface?, i: Int ->
                        //아무런 동작도 하지 않음
                    }
                )
                builder.setPositiveButton("확인",
                    { dialogInterface: DialogInterface?, i: Int ->
                        Firebase.auth.signOut()
                        val intent = Intent(this, IntroActivity::class.java)
                        finishAffinity()
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, "로그아웃에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                )
                builder.show()
                }
            }

        return true
    }
}
