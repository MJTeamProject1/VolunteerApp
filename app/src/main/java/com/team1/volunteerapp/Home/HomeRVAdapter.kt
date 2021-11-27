package com.team1.volunteerapp.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class HomeRVAdapter (private val items : MutableList<VolunteerModel>) : RecyclerView.Adapter<HomeRVAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.home_rv_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // 클릭 시
        if(itemClick != null){
            holder.itemView.setOnClickListener { view->
//                val intent = Intent(holder.itemView?.context, AboutViewActivity::class.java)
//                intent.putExtra("num", vol_num2)
//                ContextCompat.startActivity(holder.itemView.context, intent, null)

                itemClick?.onClick(view, position)
            }
        }
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // itemClick 인터페이스
    interface ItemClick{
        fun onClick(view: View, position: Int)


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
            if(item.vol_srvcClCode?.contains("편의")) {
                vol_srvcClCode.setImageResource(R.drawable.vv)
            }
            else if(item.vol_srvcClCode?.contains("문화")) {
                vol_srvcClCode.setImageResource(R.drawable.world_book_day)
            }
            else if(item.vol_srvcClCode?.contains("주거")){
                vol_srvcClCode.setImageResource(R.drawable.home)
            }
            else if(item.vol_srvcClCode?.contains("상담")){
                vol_srvcClCode.setImageResource(R.drawable.consulting)
            }
            else if(item.vol_srvcClCode?.contains("교육")){
                vol_srvcClCode.setImageResource(R.drawable.book)
            }
            else if(item.vol_srvcClCode?.contains("의료")){
                vol_srvcClCode.setImageResource(R.drawable.first_aid_kit)
            }
            else if(item.vol_srvcClCode?.contains("농어촌")){
                vol_srvcClCode.setImageResource(R.drawable.hill)
            }
            else if(item.vol_srvcClCode?.contains("환경")){
                vol_srvcClCode.setImageResource(R.drawable.environmental_protection)
            }
            else if(item.vol_srvcClCode?.contains("행정")){
                vol_srvcClCode.setImageResource(R.drawable.briefcase)
            }
            else if(item.vol_srvcClCode?.contains("안전")){
                vol_srvcClCode.setImageResource(R.drawable.prevention)
            }
            else if(item.vol_srvcClCode?.contains("공익")){
                vol_srvcClCode.setImageResource(R.drawable.healthcare)
            }
            else if(item.vol_srvcClCode?.contains("재난")){
                vol_srvcClCode.setImageResource(R.drawable.disaster)
            }

        }

//        init {
//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, AboutViewActivity::class.java)
//                intent.putExtra("num", vol_num.text.toString())
//                ContextCompat.startActivity(itemView.context, intent, null)
//
//            }
//        }
    }

}