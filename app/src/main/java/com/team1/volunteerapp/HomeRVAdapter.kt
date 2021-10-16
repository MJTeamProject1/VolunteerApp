package com.team1.volunteerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeRVAdapter (val items : MutableList<String>) : RecyclerView.Adapter<HomeRVAdapter.ViewHolder>(){


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
        private var rv_text : TextView = itemView.findViewById<TextView>(R.id.mRV_itemText)

        fun bindItems(item : String){
            rv_text.text = item

        }
    }

}