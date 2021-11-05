package com.team1.volunteerapp.Community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class CommAdapter(val CommList: ArrayList<CUser>) : RecyclerView.Adapter<CommAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comm_card_rv_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.title.text = CommList.get(position).Title
        holder.nickname.text = CommList.get(position).Nickname
        holder.contents.text = CommList.get(position).Contents

    }

    override fun getItemCount(): Int {
        return CommList.size
    }

    class CustomViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val nickname = itemView.findViewById<TextView>(R.id.tvNickName)
        val contents = itemView.findViewById<TextView>(R.id.tvContents)

    }
}