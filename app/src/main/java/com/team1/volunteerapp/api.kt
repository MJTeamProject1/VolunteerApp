package com.example.api

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class api : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var stringArray = Array(10, { item -> "" })
        println("=========1쓰레드 밖${stringArray.get(0)}")
        val job = CoroutineScope(IO).launch {
            val key: String =
                "onftKf775WKPZ9Hb+EEq96Lt12k3+cdCgp6y5+py2jelojI4mxiQLdahxxURolwnY29rJpoX1kSW6WcgUoSWSw=="
            var url: String =
                "http://openapi.1365.go.kr/openapi/service/rest/CodeInquiryService/getAreaCodeInquiryList?schSido=서울&$key"
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
                    println("=========${i + 1}=========")

                    println(
                        "1. 시/도 이름 : ${
                            elem.getElementsByTagName("sidoNm").item(0).textContent
                        }"
                    )
                    println(
                        "2. 시/도 코드 : ${
                            elem.getElementsByTagName("sidoCd").item(0).textContent
                        }"
                    )
                    println(
                        "3. 구/군 이름 : ${
                            elem.getElementsByTagName("gugunNm").item(0).textContent
                        }"
                    )
                    println(
                        "4. 구/ 군코드 : ${
                            elem.getElementsByTagName("gugunCd").item(0).textContent
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
