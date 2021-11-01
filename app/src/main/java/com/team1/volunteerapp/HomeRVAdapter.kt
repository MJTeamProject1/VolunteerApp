package com.team1.volunteerapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

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
        fun bindItems(item : VolunteerModel){

            vol_area.text = item.nanmmbyNm
            vol_context.text = item.progrmSj
            vol_start.text = item.progrmBgnde
            vol_end.text = item.progrmEndde
            vol_num.text = item.progrmRegistNo
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