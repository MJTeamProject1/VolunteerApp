package com.team1.volunteerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_home.*

class HomeRVAdapter (val items : MutableList<VolunteerModel>) : RecyclerView.Adapter<HomeRVAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRVAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.home_rv_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: HomeRVAdapter.ViewHolder, position: Int) {

        // 클릭 시
        /*
        if(itemClick != null){
            holder.itemView.setOnClickListener { view->
                val intent = Intent(holder.itemView?.context, AboutViewActivity::class.java)
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            }
        }*/

        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // itemClick 인터페이스
    interface ItemClick{
        fun onClick(view : View, position: Int)
    }
    var itemClick : ItemClick? = null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val vol_area : TextView = itemView.findViewById<TextView>(R.id.mRV_itemText_home2)
        private val vol_context : TextView = itemView.findViewById<TextView>(R.id.mRV_itemText_home)
        private val vol_start : TextView = itemView.findViewById<TextView>(R.id.mRV_itemText_home3)
        private val vol_end : TextView = itemView.findViewById<TextView>(R.id.mRV_itemText_home4)
        private val vol_num :TextView = itemView.findViewById<TextView>(R.id.vol_num)
        private val vol_srvcClCode :ImageView = itemView.findViewById<ImageView>(R.id.image_vol)
        fun bindItems(item : VolunteerModel){

            vol_area.text = item.nanmmbyNm
            vol_context.text = item.progrmSj
            vol_start.text = item.progrmBgnde
            vol_end.text = item.progrmEndde
            vol_num.text = item.progrmRegistNo

            // 이미지 설정
            if(item.vol_srvcClCode?.contains("편의") == true) {
                vol_srvcClCode.setImageResource(R.drawable.vv)
            }
            else if(item.vol_srvcClCode?.contains("문화") == true) {
                vol_srvcClCode.setImageResource(R.drawable.world_book_day)
            }
            else if(item.vol_srvcClCode?.contains("주거") == true){
                vol_srvcClCode.setImageResource(R.drawable.home)
            }
            else if(item.vol_srvcClCode?.contains("상담") == true){
                vol_srvcClCode.setImageResource(R.drawable.consulting)
            }
            else if(item.vol_srvcClCode?.contains("교육") == true){
                vol_srvcClCode.setImageResource(R.drawable.book)
            }
            else if(item.vol_srvcClCode?.contains("의료") == true){
                vol_srvcClCode.setImageResource(R.drawable.first_aid_kit)
            }
            else if(item.vol_srvcClCode?.contains("농어촌") == true){
                vol_srvcClCode.setImageResource(R.drawable.hill)
            }
            else if(item.vol_srvcClCode?.contains("환경") == true){
                vol_srvcClCode.setImageResource(R.drawable.environmental_protection)
            }
            else if(item.vol_srvcClCode?.contains("행정") == true){
                vol_srvcClCode.setImageResource(R.drawable.briefcase)
            }
            else if(item.vol_srvcClCode?.contains("안전") == true){
                vol_srvcClCode.setImageResource(R.drawable.prevention)
            }
            else if(item.vol_srvcClCode?.contains("공익") == true){
                vol_srvcClCode.setImageResource(R.drawable.healthcare)
            }
            else if(item.vol_srvcClCode?.contains("재난") == true){
                vol_srvcClCode.setImageResource(R.drawable.disaster)
            }

        }

        init {
            itemView.setOnClickListener {

                val intent = Intent(itemView.context, AboutViewActivity::class.java)
                intent.putExtra("num", vol_num.text.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }

}