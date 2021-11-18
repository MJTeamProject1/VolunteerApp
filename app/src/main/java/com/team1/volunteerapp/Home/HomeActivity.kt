package com.team1.volunteerapp.Home


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.utils.BottomNavDrawerFragment
import com.team1.volunteerapp.Community.CommActivity
import com.team1.volunteerapp.Favorite.FavoritesActivity
import com.team1.volunteerapp.Profile.ProfileActivity
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.AnimationB
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory


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
    private var sido : String? = null
    private var gugun : String? = null
    private var vol_name : String? = null
    private var vol_srvcClCode : String? = null
    //뒤로가기 연속 클릭 대기 시간
    private var mBackWait:Long = 0

    @RequiresApi(Build.VERSION_CODES.N)
    private val items_home = mutableListOf<VolunteerModel>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(bottomAppBar)

        auth = Firebase.auth

        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")


        // RecyclerView 데이터 삽입할 배열 선언
        val stringArray = Array(10) { item -> "" }
        val stringArray2 = Array(10) { item -> "" }
        val stringArray3 = Array(10) { item -> "" }
        val stringArray4 = Array(10) { item -> "" }
        val stringArray5 = Array(10) { item -> "" }
        val stringArray6 = Array(10) { item -> "" }


        // Coroutine을 이용한 API 불러오기
        // 지역 정보를 통한 봉사 정보 불러오기
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

                    // 봉사 프로그램 번호 API 호출
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        val key1 : String =
                            "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
                        val url1 : String =
                            "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrPartcptnItem?progrmRegistNo=${vol_num}&serviceKey=$key1"
                        //progrmRegistNo=2780545
                        //2780545 프로그램 번호 수정

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
                        job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
                        //join() 함수가 끝날때까지 runblocking이 지속
                    }
                    // 배열에 삽입
                    stringArray.set(i, vol_area)
                    stringArray2.set(i, vol_context)
                    stringArray3.set(i, vol_start)
                    stringArray4.set(i, vol_end)
                    stringArray5.set(i, vol_num)
                    vol_srvcClCode?.let { stringArray6.set(i, it) }
                    println("==========="+ vol_srvcClCode)
                }
            }
        }
        runBlocking {
            job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
            //join() 함수가 끝날때까지 runblocking이 지속
        }

        // item에 추가
        for (i in 0..9) {
            val vol_area = stringArray[i]
            val vol_context = stringArray2[i]
            val vol_start = stringArray3[i].substring(0,4) + "." + stringArray3[i].substring(4,6) +"." + stringArray3[i].substring(6,8)
            val vol_end = stringArray4[i].substring(0,4) + "." + stringArray4[i].substring(4,6) +"." + stringArray4[i].substring(6,8)
            val vol_num = stringArray5[i]
            val vol_srvcClCode = stringArray6[i]
            items_home.add(VolunteerModel(vol_area, vol_context, vol_start, vol_end, vol_num, vol_srvcClCode))
        }

        //RecyclerView Adapter 연결
        val home_rv = findViewById<RecyclerView>(R.id.mRecyclerViewHome)
        val home_rvAdapter = HomeRVAdapter(items_home)

        home_rv.adapter = home_rvAdapter
        home_rv.layoutManager = LinearLayoutManager(this)


        // 커뮤니티 버튼
        val testCommunityBtn = findViewById<FloatingActionButton>(R.id.fab)
        testCommunityBtn.setOnClickListener {
            fab.hide(AnimationB.addVisibilityChanged)
            Handler().postDelayed({
                val intent = Intent(this, CommActivity::class.java)
                intent.putExtra("nickname", vol_user)
                startActivity(intent)
            }, 150)
        }
    }

    override fun onStart() {
        // 애니메이션 작동
        super.onStart()
        Handler().postDelayed({
            fab.show()
        }, 450)
    }

    override fun onResume() {
        super.onResume()
        println("onresume 호출되었다")
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
                        }
                        if (doc["phonenumber"] != null) {
                            user_phone = doc["phonenumber"].toString()
                        }
                    }
                }
            }
    }

    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >= 1500){ mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"'뒤로' 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_LONG).show()
        } else { finish() }
    }

    // 화면 아래 메뉴 바
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
                val intent = Intent(this, ProfileActivity::class.java)
                if(vol_time == null){
                    vol_time = ""
                }
                if(vol_title == null){
                    vol_title = ""
                }
                intent.putExtra("time", vol_time.toString())
                intent.putExtra("title", vol_title)
                intent.putExtra("goaltime", vol_goaltime)
                intent.putExtra("nickname", vol_user)
                intent.putExtra("username", vol_name)
                intent.putExtra("email", user_email)
                intent.putExtra("phone", user_phone)

                Handler().postDelayed({
                    startActivity(intent)
                }, 300)

            }
            R.id.app_bar_fav -> {
                fab.hide(AnimationB.addVisibilityChanged)
                val intent = Intent(this, FavoritesActivity::class.java)
                Handler().postDelayed({
                    startActivity(intent)
                }, 300)
            }
        }
        return true
    }

}
