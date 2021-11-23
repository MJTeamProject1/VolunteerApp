package com.team1.volunteerapp.Profile

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
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
import com.team1.volunteerapp.R
import com.team1.volunteerapp.SettingActivity
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.passwordcheck.view.*

class ProfileActivity : AppCompatActivity() {

    val OPEN_GALLERY = 1
    private lateinit var pieChart: PieChart
    private var voltime :String? = null
    private var voltitle : String? = null
    private var volgoaltime : String? = null
    private var usernickname : String? = null
    private var pnumber : String? = null
    private var user_sido : String? = null
    private var user_gugun : String? = null
    private var user_pass : String? = null
    var name : String? = null
    var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(bottomAppBar)

        val homeButton = findViewById<FloatingActionButton>(R.id.fab)
        val profileInfo = findViewById<TextView>(R.id.profileInfo)
        val profileEmail = findViewById<TextView>(R.id.profileEmail)
        val profileName = findViewById<TextView>(R.id.profileName)
        val profileNumber = findViewById<TextView>(R.id.profileNumber)
        val profileImage = findViewById<ImageView>(R.id.profileImage)

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
        pnumber = intent.getStringExtra("phone")
        name = intent.getStringExtra("username")
        email = intent.getStringExtra("email")
        user_sido = intent.getStringExtra("usido")
        user_gugun = intent.getStringExtra("ugungu")
        user_pass = intent.getStringExtra("pass")
        pieChart = findViewById(R.id.PieChartMyVolune)

        //닉네임 설정
        profileInfo.text = usernickname
        profileEmail.text = email
        profileName.text = name
        profileNumber.text = pnumber

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
            fab.hide(AnimationB.addVisibilityChanged)
            Handler().postDelayed({
                finish()
            }, 300)
        }

        profileImage.setOnClickListener{ // 이미지 버튼을 클릭시
            //TODO 이미지 변경 버튼, 세팅 버튼을 선택하는 화면 띄우기
            Log.d("Profile", "프로필 버튼 클릭")
            val mdialogview = LayoutInflater.from(this).inflate(R.layout.passwordcheck, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mdialogview)
                .setTitle("비밀번호 확인")
            mdialogview.emailCheckArea.setText(email)
            val  mAlertDialog = mBuilder.show()
            mdialogview.dialogCheckBtn.setOnClickListener {
                Log.d("aa", user_pass.toString())
                Log.d("bb", mdialogview.passCheckArea.text.toString())
                if(mdialogview.passCheckArea.text.toString() == user_pass.toString()){
                    val intent = Intent(this, SettingActivity::class.java)
                    intent.putExtra("userEmail", email)
                    intent.putExtra("userPhone", pnumber)
                    intent.putExtra("userNickname", usernickname)
                    intent.putExtra("userGoal", volgoaltime)
                    intent.putExtra("userSido", user_sido)
                    intent.putExtra("userGungu", user_gugun)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"비밀번호를 올바르게 입력해주세요.",Toast.LENGTH_SHORT).show()
                }
            }
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, OPEN_GALLERY)
        }
    }

    //갤러리 사진 가져오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                OPEN_GALLERY -> {
                    try{
                        var uri = data?.data
                        Log.d("Profile", uri.toString())
                        profileImage.setImageURI(uri)
                    }catch (e:Exception){}
                }
            }
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
            goalpercent = 100 - timepercent
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
        colors.add(Color.parseColor("#008cb2"))
        colors.add(Color.parseColor("#FFBC1A"))

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
        pieChart.setDrawCenterText(true)
        if(voltime == ""){
            pieChart.centerText = "봉사시간 : 0시간"
        }
        else{
            pieChart.centerText = "봉사시간 : ${voltime}시간"
        }

        pieChart.invalidate()

    }

    //bottom bar

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("로그아웃")
                builder.setMessage("정말로 로그아웃 하시겠습니까?")
                builder.setNegativeButton("취소"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    //아무런 동작도 하지 않음
                }
                builder.setPositiveButton("확인"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    Firebase.auth.signOut()
                    val intent = Intent(this, IntroActivity::class.java)
                    finishAffinity()
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "로그아웃에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                }
                builder.show()
                }
            }

        return true
    }
}
