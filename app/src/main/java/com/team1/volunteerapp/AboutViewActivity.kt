package com.team1.volunteerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.team1.volunteerapp.Favorite.FavoriteModel
import com.team1.volunteerapp.Favorite.FavoritesActivity
import com.team1.volunteerapp.Profile.ProfileActivity
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
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

class AboutViewActivity : AppCompatActivity() {

    private var num1 : String = ""
    private var tel1 : String = ""
    private var title1 : String = ""
    private var starttime1 : String = ""
    private var endtime1 : String = ""
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
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    private val items_about = mutableListOf<VolunteerModel>()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutview)
        setSupportActionBar(bottomAppBar)
        auth = Firebase.auth

        val num = intent.getStringExtra("num")
        val volunteerTitle: TextView = findViewById(R.id.volunteerTitle)
        val volunteerDetail: TextView = findViewById(R.id.voluteerDetail)
        val applyButton: AppCompatButton = findViewById(R.id.applyButton)
//        val callButton: AppCompatButton = findViewById(R.id.callButton)
//        val favoriteButton = findViewById<Button>(R.id.favoriteBtn)

        // 기본 정보
        val textprogrmSttusSe = findViewById<TextView>(R.id.textprogrmSttusSe)
        val textmnnstNm = findViewById<TextView>(R.id.textmnnstNm)
        val textnoticeBgnde = findViewById<TextView>(R.id.textnoticeBgnde)
        val textnoticeEndde = findViewById<TextView>(R.id.textnoticeEndde)
        val textrcritNmpr = findViewById<TextView>(R.id.textrcritNmpr)
        val textprogrmBgnde = findViewById<TextView>(R.id.textprogrmBgnde)
        val textprogrmEndde = findViewById<TextView>(R.id.textprogrmEndde)
        val textactBeginTm = findViewById<TextView>(R.id.textactBeginTm)
        val textactEndTm = findViewById<TextView>(R.id.textactEndTm)
        val textactPlace = findViewById<TextView>(R.id.textactPlace)
        val textsrvcClCode = findViewById<TextView>(R.id.textsrvcClCode)
        val textnanmmbyNm = findViewById<TextView>(R.id.textnanmmbyNm)

        // 상세 정보
        val textnanmmbyNmAdmn = findViewById<TextView>(R.id.textnanmmbyNmAdmn)
        val texttelno = findViewById<TextView>(R.id.texttelno)
        val textpostAdres = findViewById<TextView>(R.id.textpostAdres)


        var vol_detail: String? = null
        var vol_starttime: String? = null
        var vol_endtime: String? = null
        var vol_title: String? = null



        // 기본 정보
        var vol_progrmSttusSe : String? = null
        var vol_mnnstNm : String? = null
        var vol_noticeBgnde  : String? = null
        var vol_noticeEndde : String? = null
        var vol_rcritNmpr : String? = null
        var vol_progrmBgnde  : String? = null
        var vol_progrmEndde : String? = null
        var vol_actBeginTm  : String? = null
        var vol_actEndTm : String? = null
        var vol_actPlace : String? = null
        var vol_srvcClCode : String? = null
        var vol_nanmmbyNm : String? = null

        // 연락처
        var vol_nanmmbyNmAdmn : String? = null
        var vol_telnum: String? = null
        var vol_adress : String? = null
        var vol_email: String? = null

        volunteerDetail.setMovementMethod(ScrollingMovementMethod())
        volunteerTitle.setText(num)


        // Coroutine을 이용한 API 불러오기
        val job = CoroutineScope(Dispatchers.IO).launch {
            val key: String =
                "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
            var url: String =
                //지역 정보는 현재 고정 추후 수정할 예정
                "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrPartcptnItem?progrmRegistNo=${num}&serviceKey=$key"
            //progrmRegistNo=2780545
            //2780545 프로그램 번호 수정

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

                    vol_detail = elem.getElementsByTagName("progrmCn").item(0).textContent
                    vol_starttime = elem.getElementsByTagName("actBeginTm").item(0).textContent
                    vol_endtime = elem.getElementsByTagName("actEndTm").item(0).textContent
                    vol_title = elem.getElementsByTagName("progrmSj").item(0).textContent
                    vol_email = elem.getElementsByTagName("email").item(0).textContent

                    vol_progrmSttusSe = elem.getElementsByTagName("progrmSttusSe").item(0).textContent
                    vol_mnnstNm = elem.getElementsByTagName("mnnstNm").item(0).textContent
                    vol_noticeBgnde = elem.getElementsByTagName("noticeBgnde").item(0).textContent
                    vol_noticeEndde = elem.getElementsByTagName("noticeEndde").item(0).textContent
                    vol_rcritNmpr = elem.getElementsByTagName("rcritNmpr").item(0).textContent
                    vol_progrmBgnde = elem.getElementsByTagName("progrmBgnde").item(0).textContent
                    vol_progrmEndde = elem.getElementsByTagName("progrmEndde").item(0).textContent
                    vol_actBeginTm = elem.getElementsByTagName("actBeginTm").item(0).textContent
                    vol_actEndTm = elem.getElementsByTagName("actEndTm").item(0).textContent
                    vol_actPlace = elem.getElementsByTagName("actPlace").item(0).textContent
                    vol_srvcClCode = elem.getElementsByTagName("srvcClCode").item(0).textContent
                    vol_nanmmbyNm = elem.getElementsByTagName("nanmmbyNm").item(0).textContent

                    vol_nanmmbyNmAdmn = elem.getElementsByTagName("nanmmbyNmAdmn").item(0).textContent
                    vol_telnum = elem.getElementsByTagName("telno").item(0).textContent
                    vol_adress = elem.getElementsByTagName("postAdres").item(0).textContent

                }
            }
        }
        runBlocking {
            job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
            //join() 함수가 끝날때까지 runblocking이 지속
        }
        volunteerTitle.setText(vol_title)

        if(vol_progrmSttusSe == "2"){
            textprogrmSttusSe.setText("모집중")
        } else{
            textprogrmSttusSe.setText("모집 완료")
        }
        textmnnstNm.setText(vol_mnnstNm)
        textnoticeBgnde.setText(vol_noticeBgnde)
        textnoticeEndde.setText(vol_noticeEndde)
        textrcritNmpr.setText(vol_rcritNmpr)
        textprogrmBgnde.setText(vol_progrmBgnde)
        textprogrmEndde.setText(vol_progrmEndde)
        textactBeginTm.setText(vol_actBeginTm)
        textactEndTm.setText(vol_actEndTm)
        textactPlace.setText(vol_actPlace)
        textsrvcClCode.setText(vol_srvcClCode)
        textnanmmbyNm.setText(vol_nanmmbyNm)

        volunteerDetail.setText(vol_detail)

        textnanmmbyNmAdmn.setText(vol_nanmmbyNmAdmn)
        texttelno.setText(vol_telnum)
        textpostAdres.setText(vol_adress)


        //홈으로 가기
        val homeButton = findViewById<FloatingActionButton>(R.id.fab)
        homeButton.setOnClickListener { // 홈으로 돌아가기
            fab.hide(addVisibilityChanged)
            Handler().postDelayed({
                finish()
            }, 300)
        }

        applyButton.setOnClickListener {
            db.collection("UserData")
                .get()
                .addOnSuccessListener { result ->
                    for (doc in result) {
                        if (doc["uid"] == auth.uid.toString()) {
                            val num =
                                vol_endtime.toString().toInt() - vol_starttime.toString().toInt()
                            val userauth: HashMap<String, Any> = hashMapOf(
                                "vol_time" to num,
                                "vol_title" to vol_title.toString()
                            )

                            if (doc["vol_time"] != null) {
                                userauth["vol_time"] = doc["vol_time"].toString().toInt() + num
                            }
                            if (doc["vol_title"] != null) {
                                userauth["vol_title"] = vol_title.toString() + "@${doc["vol_title"].toString()}"
                            }
                            db.collection("UserData").document(doc.id.toString())
                                .update(userauth)
                            Toast.makeText(this, "신청완료!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

//        callButton.setOnClickListener {
//            var intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse("tel:${vol_telnum}")
//            startActivity(intent)
//
//        }
//
//
//
//        // 봉사번호를 즐겨찾기에 추가하기
//        favoriteButton.setOnClickListener {
//            if (num != null) {
//
//                FBRef.favoriteRef
//                    .child(FBAuth.getUid())
//                    .child(num)
//                    .setValue(FavoriteModel(true))
//                Toast.makeText(this, "즐겨찾기 추가완료!", Toast.LENGTH_SHORT).show()
//            }
//
//        }

        tel1 = vol_telnum.toString()
        if (num != null) {
            num1 = num
        }
        title1 = vol_title.toString()
        starttime1 = vol_starttime.toString()
        endtime1 = vol_endtime.toString()
    }

    // 화면 아래 메뉴 바
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navi_menu_aboutview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.app_bar_apply -> {
                db.collection("UserData")
                    .get()
                    .addOnSuccessListener { result ->
                        for (doc in result) {
                            if (doc["uid"] == auth.uid.toString()) {
                                val num =
                                    endtime1.toString().toInt() - starttime1.toString().toInt()
                                val userauth: HashMap<String, Any> = hashMapOf(
                                    "vol_time" to num,
                                    "vol_title" to title1.toString()
                                )

                                if (doc["vol_time"] != null) {
                                    userauth["vol_time"] = doc["vol_time"].toString().toInt() + num
                                }
                                if (doc["vol_title"] != null) {
                                    userauth["vol_title"] = title1.toString() + "@${doc["vol_title"].toString()}"
                                }
                                db.collection("UserData").document(doc.id.toString())
                                    .update(userauth)
                                Toast.makeText(this, "신청완료!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }


            }
            R.id.app_bar_favadd -> {
                println("-===================" + num1)
                if (num1 != null) {
                    FBRef.favoriteRef
                        .child(FBAuth.getUid())
                        .child(num1)
                        .setValue(FavoriteModel(true))
                    Toast.makeText(this, "즐겨찾기 추가완료!", Toast.LENGTH_SHORT).show()
                }


            }
            R.id.app_bar_call -> {
                var intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${tel1}")
                startActivity(intent)
            }
        }
        return true
    }
}