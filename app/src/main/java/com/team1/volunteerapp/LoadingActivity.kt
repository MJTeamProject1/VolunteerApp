package com.team1.volunteerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.os.Handler
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
var sido : String? = null
var gugun : String? = null
var sidocode : String? = null
var guguncode : String? = null


class LoadingActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        var stringArray = Array(10, { item -> "" })
        println("=========1쓰레드 밖${stringArray.get(0)}")

        sido = intent.getStringExtra("sido")
        gugun = intent.getStringExtra("gugun")

        val job = CoroutineScope(Dispatchers.IO).launch {
            val key: String =
                "onftKf775WKPZ9Hb+EEq96Lt12k3+cdCgp6y5+py2jelojI4mxiQLdahxxURolwnY29rJpoX1kSW6WcgUoSWSw=="
            var url: String =
                "http://openapi.1365.go.kr/openapi/service/rest/CodeInquiryService/getAreaCodeInquiryList?schSido=${sido}&schGugun=${gugun}&$key"
            //http://openapi.1365.go.kr/openapi/service/rest/CodeInquiryService/getAreaCodeInquiryList?schSido=[]&gugunCd[]&인증키
            //[]칸에 회원가입시 입력하는 정보
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

                    // 지역코드
                    sidocode = elem.getElementsByTagName("sidoCd").item(0).textContent
                    guguncode = elem.getElementsByTagName("gugunCd").item(0).textContent

                }
            }
        }
        runBlocking {
            job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
            //join() 함수가 끝날때까지 runblocking이 지속
        }

        Handler().postDelayed({
            val Intent = Intent(this, HomeActivity::class.java)
            Intent.putExtra("sido",sidocode)
            Intent.putExtra("gugun",guguncode)
            startActivity(Intent)
            finish()
        }, 1000)
    }
}
