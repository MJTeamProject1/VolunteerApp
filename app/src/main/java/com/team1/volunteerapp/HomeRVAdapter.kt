package com.team1.volunteerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeRVAdapter (val items : MutableList<VolunteerModel>) : RecyclerView.Adapter<HomeRVAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRVAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.home_rv_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: HomeRVAdapter.ViewHolder, position: Int) {

        // 클릭 시
        if(itemClick != null){
            holder.itemView.setOnClickListener { view->
                itemClick?.onClick(view,position)
            }
        }

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

        fun bindItems(item : VolunteerModel){

            vol_area.text = item.nanmmbyNm
            vol_context.text = item.progrmSj
            vol_start.text = item.progrmBgnde
            vol_end.text = item.progrmEndde

        }
    }

}