package com.team1.volunteerapp.Favorite

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

class FavoriteRVAdapter (val items : MutableList<String>) : RecyclerView.Adapter<FavoriteRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.favorite_rv_item, parent, false)

        return ViewHolder(view1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @RequiresApi(Build.VERSION_CODES.N)
        fun bindItems(item : String){
            // Coroutine을 이용한 API 불러오기
            val job = CoroutineScope(Dispatchers.IO).launch {

                val key: String =
                    "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
                var url: String =
                    //지역 정보는 현재 고정 추후 수정할 예정
                    "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrPartcptnItem?progrmRegistNo=${item}&serviceKey=$key"

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

                        var rv_text1 = itemView.findViewById<TextView>(R.id.mRV_itemText_home)
                        var rv_text2 = itemView.findViewById<TextView>(R.id.mRV_itemText_home2)
                        var rv_text3 = itemView.findViewById<TextView>(R.id.mRV_itemText_home3)
                        var rv_text4 = itemView.findViewById<TextView>(R.id.mRV_itemText_home4)

                        rv_text1.text = vol_context
                        rv_text2.text = vol_area
                        rv_text3.text = vol_start
                        rv_text4.text = vol_end
                    }
                }
            }
            runBlocking {
                job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
                //join() 함수가 끝날때까지 runblocking이 지속
            }
        }
    }
}

