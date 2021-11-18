package com.team1.volunteerapp.Favorite

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(items[position])

    }



    override fun getItemCount(): Int {
        return items.size
        notifyDataSetChanged()
    }

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }
    var itemClick : ItemClick? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @RequiresApi(Build.VERSION_CODES.N)
        fun bindItems(item : String){

            val del = itemView.findViewById<ImageView>(R.id.delBtn)
            del.setOnClickListener{
                Log.d("---------------" , item)
                if(items.contains(item)){
                    //즐겨찾기 삭제
                    FBRef.favoriteRef
                        .child(FBAuth.getUid())
                        .child(item)
                        .removeValue()
                    items.clear()
                    notifyDataSetChanged()
                }
            }

            // Coroutine을 이용한 API 불러오기
            val job = CoroutineScope(Dispatchers.IO).launch {

                val key: String =
                    "WbB5cwZvKLInWD4JmJjDBvuuInA6+7ufo7RHGngZH7+UEAaSVc4x5UsvdFIx4NPg+MPlSUvet1IBhzr6Ly6Diw=="
                val url: String =
                    //지역 정보는 현재 고정 추후 수정할 예정
                    "http://openapi.1365.go.kr/openapi/service/rest/VolunteerPartcptnService/getVltrPartcptnItem?progrmRegistNo=${item}&serviceKey=$key"

                val xml: Document =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)

                xml.documentElement.normalize()
                println("Root element :" + xml.documentElement.nodeName)

                val list: NodeList = xml.getElementsByTagName("item")

                // 등록번호가 사라졌을 때 예외처리
                try {
                    for (i in 0 until list.length) {
                        var n: Node = list.item(i)
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

                            val rv_text1 = itemView.findViewById<TextView>(R.id.mRV_itemText_home)
                            val rv_text2 = itemView.findViewById<TextView>(R.id.mRV_itemText_home2)
                            val rv_text3 = itemView.findViewById<TextView>(R.id.mRV_itemText_home3)
                            val rv_text4 = itemView.findViewById<TextView>(R.id.mRV_itemText_home4)

                            rv_text1.text = vol_context
                            rv_text2.text = vol_area
                            rv_text3.text = vol_start.toString().substring(0,4) + "." + vol_start.toString().substring(4,6) + "." + vol_start.toString().substring(6,8)
                            rv_text4.text = vol_end.toString().substring(0,4) + "." + vol_end.toString().substring(4,6) + "." + vol_end.toString().substring(6,8)
                        }
                    }
                }catch (e : Exception){
                    Log.d("asdf","등록번호가 존재하지 않음")
                }

            }
            runBlocking {
                job.join() //suspend에서만 작동하기때문에 runblocking안에 넣는다
                //join() 함수가 끝날때까지 runblocking이 지속
            }



        }
    }
}

