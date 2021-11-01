package com.team1.volunteerapp.Community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class CommAdapter(val CommList: ArrayList<CUser>) : RecyclerView.Adapter<CommAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.community_rv_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.name.text = CommList.get(position).name
        holder.greet.text = CommList.get(position).greet
        holder.index.text = CommList.get(position).index.toString()
    }

    override fun getItemCount(): Int {
        return CommList.size
    }

    class CustomViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.tvName)
        val greet = itemView.findViewById<TextView>(R.id.tvGreet)
        val index = itemView.findViewById<TextView>(R.id.tvIndex)
    }
}