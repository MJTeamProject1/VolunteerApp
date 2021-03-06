package com.team1.volunteerapp.Profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
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
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.team1.volunteerapp.Auth.IntroActivity
import com.team1.volunteerapp.R
import com.team1.volunteerapp.SettingActivity
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.passwordcheck.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import android.graphics.Typeface




class ProfileActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private var voltime :String? = null
    private var voltitle : String? = null
    private var volgoaltime : String? = null
    private var usernickname : String? = null
    private var pnumber : String? = null
    private var user_sido : String? = null
    private var user_gugun : String? = null
    private var user_pass : String? = null
    private var user_img : String? = null
    private var whentime : String? = null
    private var user_date : String? = null
    var name : String? = null
    var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(bottomAppBar)

        // ?????? ???
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()

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
        user_img = intent.getStringExtra("proImage")
        user_date = intent.getStringExtra("date")
        whentime = intent.getStringExtra("applywhen")
        pieChart = findViewById(R.id.PieChartMyVolune)

        if(FBAuth.getUserImage() == null){
            //?????? ?????????
        }
        else{
            val bitmap = FBAuth.getUserImage()
            profileImage.setImageBitmap(bitmap)
        }

        //????????? ??????
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


        //RecyclerView Adapter ??????
        val profile_rv = findViewById<RecyclerView>(R.id.mRecyclerViewProfile)
        val profile_rvAdapter = ProfileRVAdapter(items)

        profile_rv.adapter = profile_rvAdapter
        profile_rv.layoutManager = LinearLayoutManager(this)

        //????????? ??????
        val dividerItemDecoration =
            DividerItemDecoration(profile_rv.context, LinearLayoutManager(this).orientation)

        profile_rv.addItemDecoration(dividerItemDecoration)

        homeButton.setOnClickListener { // ????????? ????????????
            fab.hide(AnimationB.addVisibilityChanged)
            Handler().postDelayed({
                finish()
            }, 300)
        }

        profileImage.setOnClickListener{ // ????????? ????????? ?????????
            Log.d("Profile", "????????? ?????? ??????")
        }
    }

    //Pie Chart ??????
    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = "???????????? ??????"
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
            dataEntries.add(PieEntry(0f, "????????????"))
            goalpercent = 100 - timepercent
        }
        else{
            timepercent = (voltime.toString().toFloat() / volgoaltime.toString().toFloat() * 100)
            goalpercent = 100 - timepercent
            if(goalpercent < 0){
                timepercent = 100F
                goalpercent = 0F
            }
            dataEntries.add(PieEntry(timepercent, "????????????"))
        }
        dataEntries.add(PieEntry(goalpercent, "????????????")) // ????????? ?????? ?????????????????? -??????

        val colors: ArrayList<Int> = ArrayList() // ??? ????????? ??????
        colors.add(Color.parseColor("#008cb2"))
        colors.add(Color.parseColor("#FFBC1A"))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // %??? ??????
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(15f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //????????? ??? ??????
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(0xFFD98F) // ?????? ?????? ??????

        //??? ????????? ?????????
        pieChart.setDrawCenterText(true)
        if(voltime == ""){
            val voltimeset = findViewById<TextView>(R.id.voltimeset)
            voltimeset.text = 0.toString()
//            pieChart.centerText = "???????????? : 0??????"
        }
        else{
            val voltimeset = findViewById<TextView>(R.id.voltimeset)
            voltimeset.text = voltime.toString()
//            pieChart.centerText = "???????????? : ${voltime}??????"
        }

        pieChart.invalidate()


    }
    // ?????? ?????? ?????? ???
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu_profile, menu)
        return true
    }

    //bottom bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {

                }
            R.id.app_bar_calender->{
                //????????? ?????? ???
                val intent = Intent(this, CalenderActivity::class.java)
                intent.putExtra("user_date", user_date)
                intent.putExtra("date_title", voltitle)
                intent.putExtra("applywhen",whentime)
                startActivity(intent)
            }
            R.id.app_bar_setting ->{
                val mdialogview = LayoutInflater.from(this).inflate(R.layout.passwordcheck, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mdialogview)
                    .setTitle("???????????? ??????")
                mdialogview.emailCheckArea.setText(email)
                val  mAlertDialog = mBuilder.show()
                mdialogview.dialogCheckBtn.setOnClickListener {
                    Log.d("aa", user_pass.toString())
                    Log.d("bb", mdialogview.passCheckArea.text.toString())
                    if(mdialogview.passCheckArea.text.toString() == user_pass.toString()) {
                        val intent = Intent(this, SettingActivity::class.java)
                        intent.putExtra("userEmail", email)
                        intent.putExtra("userPhone", pnumber)
                        intent.putExtra("userNickname", usernickname)
                        intent.putExtra("userGoal", volgoaltime)
                        intent.putExtra("userSido", user_sido)
                        intent.putExtra("userGungu", user_gugun)
                        mAlertDialog.dismiss()
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"??????????????? ???????????? ??????????????????.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.app_bar_loggout ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("????????????")
                builder.setMessage("????????? ???????????? ???????????????????")
                builder.setNegativeButton("??????"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    //????????? ????????? ?????? ??????
                }
                builder.setPositiveButton("??????"
                ) { dialogInterface: DialogInterface?, i: Int ->
                    Firebase.auth.signOut()
                    val intent = Intent(this, IntroActivity::class.java)
                    finishAffinity()
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }
        }

        return true
    }
}
