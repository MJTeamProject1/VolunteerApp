package com.team1.volunteerapp.Home


import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.AboutViewActivity
import com.team1.volunteerapp.Chat.ChatRoom.ChatRoomListActivity
import com.team1.volunteerapp.Community.*
import com.team1.volunteerapp.utils.BottomNavDrawerFragment
import com.team1.volunteerapp.Favorite.FavoritesActivity
import com.team1.volunteerapp.Profile.ProfileActivity
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.AnimationB
import com.team1.volunteerapp.utils.FBAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {

    private var user_phone : String? = null
    private var user_sido : String? = null
    private var user_gugun : String? = null
    private var user_email : String? = null
    private lateinit var bottomnavdrawerfragment : BottomNavDrawerFragment

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var vol_time: String? = null
    private var vol_title: String? = null
    private var vol_goaltime: String? = null
    private var vol_user: String? = null
    private var user_pass: String? = null
    private var sido : String? = null
    private var gugun : String? = null
    private var vol_name : String? = null
    private var vol_srvcClCode : String? = null
    private var vol_date : String? = null
    private var whentime : String? = null
    //???????????? ?????? ?????? ?????? ??????
    private var mBackWait:Long = 0

    @RequiresApi(Build.VERSION_CODES.N)
    private val items_home = mutableListOf<VolunteerModel>()

    private val volNumberKeyList = Array(10){ item -> "" }
    // ??????????????????
    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open)}
    private val rotateClose : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.rotate_close)}
    private val fromBottom : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
    private var clicked = false
    private var nickHome = ""
    private lateinit var pieChart: PieChart

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(bottomAppBar)
        setVisibility(true)
        auth = Firebase.auth

        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")

        // ?????? ??? ?????? ??????
        today()
        val firstcard = findViewById<CardView>(R.id.firstCard)
        firstcard.setOnClickListener {

            fab.hide(AnimationB.addVisibilityChanged)
            if(clicked){onAddButtonClicked()}
            val intent = Intent(this, ProfileActivity::class.java)
            if(vol_time == null){
                vol_time = ""
            }
            if(vol_title == null){
                vol_title = ""
            }
            intent.putExtra("time", vol_time.toString())
            intent.putExtra("title", vol_title)
            intent.putExtra("date", vol_date)
            intent.putExtra("goaltime", vol_goaltime)
            intent.putExtra("nickname", vol_user)
            intent.putExtra("username", vol_name)
            intent.putExtra("email", user_email)
            intent.putExtra("phone", user_phone)
            intent.putExtra("usido", user_sido)
            intent.putExtra("pass", user_pass)
            intent.putExtra("ugungu", user_gugun)
            intent.putExtra("applywhen", whentime)

            Handler().postDelayed({
                startActivity(intent)
            }, 300)

        }


        // ?????????
        pieChart = findViewById(R.id.PieChartMyVolune22)

        // ?????? ???
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        val bottomBarBackground = bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(RelativeCornerSize(0.4f))
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.4f))
            .build()


        // RecyclerView ????????? ????????? ?????? ??????
        val stringArray = Array(10) { item -> "" }
        val stringArray2 = Array(10) { item -> "" }
        val stringArray3 = Array(10) { item -> "" }
        val stringArray4 = Array(10) { item -> "" }
        val stringArray5 = Array(10) { item -> "" }
        val stringArray6 = Array(10) { item -> "" }

        // Coroutine??? ????????? API ????????????
        // ?????? ????????? ?????? ?????? ?????? ????????????
        val job = CoroutineScope(Dispatchers.IO).launch {

            val key: String =
                "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
            val url: String =
                "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrAreaList?schSido=${sido}&schSign1=${gugun}&serviceKey=$key"

            val xml: Document =
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)

            xml.documentElement.normalize()
            println("Root element :" + xml.documentElement.nodeName)

            val list: NodeList = xml.getElementsByTagName("item")

            for (i in 0 until list.length) {
                val n: Node = list.item(i)
                if (n.nodeType == Node.ELEMENT_NODE) {
                    val elem = n as Element
                    val map = mutableMapOf<String, String>()

                    for (j in 0 until elem.attributes.length) {

                        map.putIfAbsent(
                            elem.attributes.item(j).nodeName,
                            elem.attributes.item(j).nodeValue
                        )
                    }

                    val vol_area = elem.getElementsByTagName("nanmmbyNm").item(0).textContent
                    val vol_context = elem.getElementsByTagName("progrmSj").item(0).textContent
                    val vol_start = elem.getElementsByTagName("progrmBgnde").item(0).textContent
                    val vol_end = elem.getElementsByTagName("progrmEndde").item(0).textContent
                    val vol_num = elem.getElementsByTagName("progrmRegistNo").item(0).textContent

                    // ?????? ???????????? ?????? API ??????
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        val key1 : String =
                            "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
                        val url1 : String =
                            "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrPartcptnItem?progrmRegistNo=${vol_num}&serviceKey=$key1"
                        //progrmRegistNo=2780545
                        //2780545 ???????????? ?????? ??????

                        val xml: Document =
                            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url1)

                        xml.documentElement.normalize()
                        println("Root element :" + xml.documentElement.nodeName)

                        val list: NodeList = xml.getElementsByTagName("item")

                        for (i in 0 until list.length) {
                            val n: Node = list.item(i)
                            if (n.nodeType == Node.ELEMENT_NODE) {
                                val elem = n as Element
                                val map = mutableMapOf<String, String>()

                                for (j in 0 until elem.attributes.length) {

                                    map.putIfAbsent(
                                        elem.attributes.item(j).nodeName,
                                        elem.attributes.item(j).nodeValue
                                    )
                                }

                                vol_srvcClCode = elem.getElementsByTagName("srvcClCode").item(0).textContent
                                println(vol_srvcClCode)
                            }
                        }
                    }
                    runBlocking {
                        job.join() //suspend????????? ????????????????????? runblocking?????? ?????????
                        //join() ????????? ??????????????? runblocking??? ??????
                    }
                    // ????????? ??????
                    stringArray.set(i, vol_area)
                    stringArray2.set(i, vol_context)
                    stringArray3.set(i, vol_start)
                    stringArray4.set(i, vol_end)
                    stringArray5.set(i, vol_num)
                    volNumberKeyList.set(i,vol_num)
                    vol_srvcClCode?.let { stringArray6.set(i, it) }
                    println("==========="+ vol_srvcClCode)
                }
            }
        }
        runBlocking {
            job.join() //suspend????????? ????????????????????? runblocking?????? ?????????
            //join() ????????? ??????????????? runblocking??? ??????
        }

        // item??? ??????
        for (i in 0..9) {
            val vol_area = stringArray[i]
            val vol_context = stringArray2[i]
            val vol_start = stringArray3[i].substring(0,4) + "." + stringArray3[i].substring(4,6) +"." + stringArray3[i].substring(6,8)
            val vol_end = stringArray4[i].substring(0,4) + "." + stringArray4[i].substring(4,6) +"." + stringArray4[i].substring(6,8)
            val vol_num = stringArray5[i]
            val vol_srvcClCode = stringArray6[i]
            items_home.add(VolunteerModel(vol_area, vol_context, vol_start, vol_end, vol_num, vol_srvcClCode))
        }

        //RecyclerView Adapter ??????
        val home_rv = findViewById<RecyclerView>(R.id.mRecyclerViewHome)
        val home_rvAdapter = HomeRVAdapter(items_home)

        home_rv.adapter = home_rvAdapter
        home_rv.layoutManager = LinearLayoutManager(this)
//        home_rv.layoutManager = GridLayoutManager(this,2)


        // ???????????? ??????
        val addBtn = findViewById<FloatingActionButton>(R.id.fab)
        addBtn.setOnClickListener {
            onAddButtonClicked()
        }

        floatingActionButton.setOnClickListener {
            fab.hide(AnimationB.addVisibilityChanged)
            onAddButtonClicked()
            Handler().postDelayed({
                val intent = Intent(this, CommunityActivity::class.java)
                intent.putExtra("nickname", vol_user)
                startActivity(intent)
            }, 250)
        }
        floatingActionButton2.setOnClickListener {
            fab.hide(AnimationB.addVisibilityChanged)
            onAddButtonClicked()
            Handler().postDelayed({
                val intent = Intent(this, ChatRoomListActivity::class.java)
                intent.putExtra("nickname", vol_user)
                startActivity(intent)
            }, 250)
        }
        floatingActionButton3.setOnClickListener {
            fab.hide(AnimationB.addVisibilityChanged)
            if(clicked){onAddButtonClicked()}
            val intent = Intent(this, FavoritesActivity::class.java)
            Handler().postDelayed({
                startActivity(intent)
            }, 250)
        }


        home_rvAdapter.itemClick = object : HomeRVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                //???????????? ????????? ??????
                fab.hide(AnimationB.addVisibilityChanged)
                if(clicked){onAddButtonClicked()}
                val intent = Intent(view.context,AboutViewActivity::class.java)
                intent.putExtra("num", volNumberKeyList[position])
                Handler().postDelayed({
                    startActivity(intent)
                }, 150)
            }
        }
    }

    // ?????? ?????? ?????? ???
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked

    }

    // ?????? ?????? ????????? ??? ??????
    private fun setVisibility(clicked: Boolean) {
        if(!clicked){
            floatingActionButton.visibility = View.VISIBLE
            floatingActionButton2.visibility = View.VISIBLE
            floatingActionButton3.visibility = View.VISIBLE
        }else{
            floatingActionButton.visibility = View.INVISIBLE
            floatingActionButton2.visibility = View.INVISIBLE
            floatingActionButton3.visibility = View.INVISIBLE
        }

    }

    // ?????? ?????? ???????????????
    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            floatingActionButton.startAnimation(fromBottom)
            floatingActionButton2.startAnimation(fromBottom)
            floatingActionButton3.startAnimation(fromBottom)
            fab.startAnimation(rotateOpen)
        }
        else{
            floatingActionButton.startAnimation(toBottom)
            floatingActionButton2.startAnimation(toBottom)
            floatingActionButton3.startAnimation(toBottom)
            fab.startAnimation(rotateClose)
        }

    }

    // ?????? ?????? ?????? ?????????
    private fun setClickable(clicked: Boolean) {
        if(!clicked){
            floatingActionButton.isClickable = true
            floatingActionButton2.isClickable = true
            floatingActionButton3.isClickable = true
        }
        else{
            floatingActionButton.isClickable = false
            floatingActionButton2.isClickable = false
            floatingActionButton3.isClickable = false
        }

    }

    override fun onStart() {
        // ??????????????? ??????
        super.onStart()
        clicked = false
        Handler().postDelayed({
            fab.show()
        }, 450)
    }

    override fun onResume() {
        super.onResume()
        println("onresume ???????????????")
        db.collection("UserData")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    if (doc["uid"] == auth.uid.toString()) {

                        if (doc["vol_time"] != null) {
                            vol_time = doc["vol_time"].toString()
                        }
                        if (doc["vol_title"] != null) {
                            vol_title = doc["vol_title"].toString()
                        }
                        if (doc["timedata"] != null) {
                            vol_goaltime = doc["timedata"].toString()
                        }
                        if (doc["nickname"] != null) {
                            vol_user = doc["nickname"].toString()
                            nickHome = vol_user as String
                            setNick()
                        }
                        if (doc["email"] != null) {
                            user_email = doc["email"].toString()
                        }
                        if (doc["username"] != null) {
                            vol_name = doc["username"].toString()
                        }
                        if (doc["sidodata"] != null) {
                            user_sido = doc["sidodata"].toString()
                        }
                        if (doc["gugundata"] != null) {
                            user_gugun = doc["gugundata"].toString()

                            val gugunText = findViewById<TextView>(R.id.gugun)
                            gugunText.setText(user_gugun)
                        }
                        if (doc["phonenumber"] != null) {
                            user_phone = doc["phonenumber"].toString()
                        }
                        if (doc["passworld"] != null) {
                            user_pass = doc["passworld"].toString()
                        }
                        if (doc["vol_applyDate"] != null) {
                            vol_date = doc["vol_applyDate"].toString()
                        }
                        if (doc["whentime"] != null) {
                            whentime = doc["whentime"].toString()
                        }

                        initPieChart()
                        try {
                            setDataToPieChart(vol_time,vol_goaltime)
                        }catch (e : Exception){
                            val v = "0"
                            setDataToPieChart(v,vol_goaltime)
                        }



                    }

                }
                FBAuth.runImage(user_email)

            }
    }

    private fun setNick() {
        val homeNick = findViewById<TextView>(R.id.homeNickText)
        homeNick.text = nickHome
    }

    override fun onBackPressed() {
        // ???????????? ?????? ??????
        if(System.currentTimeMillis() - mBackWait >= 1500){ mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"'??????' ????????? ?????? ??? ???????????? ???????????????.",Toast.LENGTH_LONG).show()
        } else { finish() }
    }

    // ?????? ?????? ?????? ???
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                bottomnavdrawerfragment = BottomNavDrawerFragment()
                bottomnavdrawerfragment.show(supportFragmentManager,
                    BottomNavDrawerFragment().tag)
                bottomnavdrawerfragment.setIntent(user_email, user_phone, vol_user, vol_goaltime, user_sido, user_gugun)
            }

            R.id.app_bar_profile -> {
                fab.hide(AnimationB.addVisibilityChanged)
                if(clicked){onAddButtonClicked()}
                val intent = Intent(this, ProfileActivity::class.java)
                if(vol_time == null){
                    vol_time = ""
                }
                if(vol_title == null){
                    vol_title = ""
                }
                intent.putExtra("time", vol_time.toString())
                intent.putExtra("title", vol_title)
                intent.putExtra("date", vol_date)
                intent.putExtra("goaltime", vol_goaltime)
                intent.putExtra("nickname", vol_user)
                intent.putExtra("username", vol_name)
                intent.putExtra("email", user_email)
                intent.putExtra("phone", user_phone)
                intent.putExtra("usido", user_sido)
                intent.putExtra("pass", user_pass)
                intent.putExtra("ugungu", user_gugun)
                intent.putExtra("applywhen", whentime)

                Handler().postDelayed({
                    startActivity(intent)
                }, 300)

            }
        }
        return true
    }

    //Pie Chart ??????
    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.isRotationEnabled = false
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        pieChart.legend.setDrawInside(false)
        pieChart.legend.isWordWrapEnabled = true


    }

    private fun setDataToPieChart(voltime: String?, volgoaltime: String?) {
        var timepercent : Float = 0F
        var goalpercent : Float = 0F
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        if(voltime == null){
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
        colors.add(Color.parseColor("#096def"))
        colors.add(Color.parseColor("#F69110"))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // %??? ??????
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(0f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        //????????? ??? ??????
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(-0x002670) // ?????? ?????? ??????

        //??? ????????? ?????????
//        pieChart.setDrawCenterText(true)
//        if(voltime == ""){
//            pieChart.centerText = "???????????? : 0??????"
//        }
//        else{
//            pieChart.centerText = "???????????? : ${voltime}??????"
//        }

        pieChart.invalidate()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun today(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM??? dd???(E)")
        val formatted = current.format(formatter)

        val today = findViewById<TextView>(R.id.today)
        today.text = formatted
    }
}
