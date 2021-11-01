package com.team1.volunteerapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
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
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutview)

        val num = intent.getStringExtra("num")
        val volunteerTitle : TextView = findViewById(R.id.volunteerTitle)
        volunteerTitle.setText(num)

        // Coroutine을 이용한 API 불러오기
        val job = CoroutineScope(Dispatchers.IO).launch {
            val key: String =
                "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
            var url: String =
                //지역 정보는 현재 고정 추후 수정할 예정
                "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrPartcptnItem?progrmRegistNo=2780545&serviceKey=$key"
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

                    println(
                        "[1]. 봉사내용 : ${
                            elem.getElementsByTagName("progrmCn").item(0).textContent
                        }"
                    )

                    println(
                        "[2]. 봉사시간 : ${
                            elem.getElementsByTagName("actBeginTm").item(0).textContent
                        }시 ~ ${
                            elem.getElementsByTagName("actEndTm").item(0).textContent
                        }시"
                    )

                    println(
                        "[3]. 담당자번호 : ${
                            elem.getElementsByTagName("telno").item(0).textContent
                        }"
                    )

                    println(
                        "[4]. 봉사제목 : ${
                            elem.getElementsByTagName("progrmSj").item(0).textContent
                        }"
                    )

                    println(
                        "[5]. 담당자이메일 : ${
                            elem.getElementsByTagName("email").item(0).textContent
                        }"
                    )
                }
            }
        }
        runBlocking {
            job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
            //join() 함수가 끝날때까지 runblocking이 지속
        }
    }
}