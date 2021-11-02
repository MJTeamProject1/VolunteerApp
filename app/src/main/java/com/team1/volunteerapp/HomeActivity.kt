package com.team1.volunteerapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Community.CommActivity
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
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    var vol_time: String? = null
    var vol_title: String? = null
    var vol_goaltime: String? = null

    var sido : String? = null
    var gugun : String? = null

    @RequiresApi(Build.VERSION_CODES.N)
    private val items_home = mutableListOf<VolunteerModel>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = Firebase.auth

        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")

        println("7777777777777777777777"+sido)
        println("7777777777777777777777"+ gugun)

        //db에서 데이터 받아오기
        // RecyclerView 데이터 삽입할 배열 선언
        var stringArray = Array(10, { item -> "" })
        var stringArray2 = Array(10, { item -> "" })
        var stringArray3 = Array(10, { item -> "" })
        var stringArray4 = Array(10, { item -> "" })
        var stringArray5 = Array(10, { item -> "" })



        // Coroutine을 이용한 API 불러오기
        val job = CoroutineScope(Dispatchers.IO).launch {

            val key: String =
                "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
            var url: String =
                //지역 정보는 현재 고정 추후 수정할 예정
                "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrAreaList?schSido=${sido}&schSign1=${gugun}&serviceKey=$key"

            val xml: Document =
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)

            xml.documentElement.normalize()
            println("Root element :" + xml.documentElement.nodeName)

            val list: NodeList = xml.getElementsByTagName("item")

            for (i in 0..list.length - 1) {
                var n: Node = list.item(i)
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    val elem = n as Element
                    val map = mutableMapOf<String, String>()

                    for (j in 0..elem.attributes.length - 1) {

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

                    // 배열에 삽입
                    stringArray.set(i, vol_area)
                    stringArray2.set(i, vol_context)
                    stringArray3.set(i, vol_start)
                    stringArray4.set(i, vol_end)
                    stringArray5.set(i, vol_num)
                }
            }
        }
        runBlocking {
            job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
            //join() 함수가 끝날때까지 runblocking이 지속
        }

        // item에 추가
        for (i in 0..9) {
            var vol_area = stringArray[i]
            var vol_context = stringArray2[i]
            var vol_start = stringArray3[i]
            var vol_end = stringArray4[i]
            var vol_num = stringArray5[i]
            items_home.add(VolunteerModel(vol_area, vol_context, vol_start, vol_end, vol_num))
        }

        //ViewPager연결
        val viewPager = findViewById<ViewPager2>(R.id.adPager)
        viewPager.adapter = ViewPagerAdapter(getBannerList())
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //RecyclerView Adapter 연결
        val home_rv = findViewById<RecyclerView>(R.id.mRecyclerViewHome)
        val home_rvAdapter = HomeRVAdapter(items_home)

        home_rv.adapter = home_rvAdapter
        home_rv.layoutManager = LinearLayoutManager(this)

        // RecyclerView item을 클릭 시
        /*
        home_rvAdapter.itemClick= object : HomeRVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                val intent = Intent(baseContext, AboutViewActivity::class.java)
                startActivity(intent)
            }
        }*/


        val testAboutViewBtn = findViewById<Button>(R.id.mAboutViewTestBtn)
        testAboutViewBtn.setOnClickListener {
            val intent = Intent(this, AboutViewActivity::class.java)
            startActivity(intent)
        }

        val testProfileBtn = findViewById<ImageButton>(R.id.mProfileBtn)
        testProfileBtn.setOnClickListener {
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
            startActivity(intent)

        }

        val testCommunityBtn = findViewById<Button>(R.id.commbtn)
        testCommunityBtn.setOnClickListener {
            val intent = Intent(this, CommActivity::class.java)
            startActivity(intent)
        }


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
                    }
                }
            }
    }

    private fun getBannerList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.pagerex, R.drawable.volunteersample)
    }
}
