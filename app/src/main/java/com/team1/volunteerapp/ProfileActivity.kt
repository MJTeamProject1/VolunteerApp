package com.team1.volunteerapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class ProfileActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val homeButton = findViewById<ImageButton>(R.id.homeButton)


        pieChart = findViewById(R.id.PieChartMyVolune)

        initPieChart()

        setDataToPieChart()

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
        val profile_rv = findViewById<RecyclerView>(R.id.mRecyclerViewProfile)
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

        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(100f, "봉사시간"))
        dataEntries.add(PieEntry(5f, "남은시간")) // 입력된 목표 봉사시간에서 -하기

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
        pieChart.centerText = "봉사시간 : XXX시간"



        pieChart.invalidate()

    }

}
