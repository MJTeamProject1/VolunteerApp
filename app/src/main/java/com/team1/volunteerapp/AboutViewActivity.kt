package com.team1.volunteerapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        auth = FirebaseAuth.getInstance()
        val num = intent.getStringExtra("num")
        val volunteerTitle : TextView = findViewById(R.id.volunteerTitle)
        val volunteerDetail : TextView = findViewById(R.id.voluteerDetail)
        volunteerTitle.setText(num)

        //배열 선언
        var stringArray = Array(10, { item -> "" })
        var stringArray2 = Array(10, { item -> "" })
        var stringArray3 = Array(10, { item -> "" })
        var stringArray4 = Array(10, { item -> "" })
        var stringArray5 = Array(10, { item -> "" })
        var stringArray6 = Array(10, { item -> "" })

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

                    val vol_detail = elem.getElementsByTagName("progrmCn").item(0).textContent
                    val vol_starttime = elem.getElementsByTagName("actBeginTm").item(0).textContent
                    val vol_endtime = elem.getElementsByTagName("actEndTm").item(0).textContent
                    val vol_telnum = elem.getElementsByTagName("telno").item(0).textContent
                    val vol_title = elem.getElementsByTagName("progrmSj").item(0).textContent
                    val vol_email = elem.getElementsByTagName("email").item(0).textContent

                    // 배열에 삽입
                    stringArray.set(i,vol_detail)
                    stringArray2.set(i,vol_starttime)
                    stringArray3.set(i,vol_endtime)
                    stringArray4.set(i,vol_telnum)
                    stringArray5.set(i,vol_title)
                    stringArray6.set(i,vol_email)

                }
            }
        }
        runBlocking {
            job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
            //join() 함수가 끝날때까지 runblocking이 지속
        }
        volunteerTitle.setText(stringArray5[0])
        volunteerDetail.setText(stringArray[0])
    }
}