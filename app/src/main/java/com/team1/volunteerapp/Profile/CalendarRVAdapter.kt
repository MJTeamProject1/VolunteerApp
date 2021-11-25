package com.team1.volunteerapp.Profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class CalendarRVAdapter (private val items : MutableList<CalendarModel> ): RecyclerView.Adapter<CalendarRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.calender_rv_item, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var caltitle = itemView.findViewById<TextView>(R.id.mRV_itemText_title)
        private var calstart = itemView.findViewById<TextView>(R.id.mRV_itemText_start)
        private var calend = itemView.findViewById<TextView>(R.id.mRV_itemText_end)

        fun bindItems(item : CalendarModel){
            caltitle.text = item.title
            calstart.text = item.starttime
            calend.text = item.endtime

        }
    }
}

