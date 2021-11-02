package com.team1.volunteerapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    private val items_about = mutableListOf<VolunteerModel>()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutview)
        auth = Firebase.auth

        val num = intent.getStringExtra("num")
        val volunteerTitle: TextView = findViewById(R.id.volunteerTitle)
        val volunteerDetail: TextView = findViewById(R.id.voluteerDetail)
        val applyButton: AppCompatButton = findViewById(R.id.applyButton)
        val favoriteButton = findViewById<Button>(R.id.favoriteBtn)

        var vol_detail: String? = null
        var vol_starttime: String? = null
        var vol_endtime: String? = null
        var vol_telnum: String? = null
        var vol_title: String? = null
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
                    vol_telnum = elem.getElementsByTagName("telno").item(0).textContent
                    vol_title = elem.getElementsByTagName("progrmSj").item(0).textContent
                    vol_email = elem.getElementsByTagName("email").item(0).textContent


                }
            }
        }
        runBlocking {
            job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
            //join() 함수가 끝날때까지 runblocking이 지속
        }
        volunteerTitle.setText(vol_title)
        volunteerDetail.setText(vol_detail)

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

        favoriteButton.setOnClickListener {

        }
    }
}