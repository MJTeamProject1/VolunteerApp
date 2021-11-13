package com.team1.volunteerapp.Community

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBAuth
import com.team1.volunteerapp.utils.FBRef

class CommAdapter(val CommList: ArrayList<CUser>) : RecyclerView.Adapter<CommAdapter.CustomViewHolder>() {
    //추가
    private lateinit var mListener : onItemClickListener


    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setonItemClickListener(listener: onItemClickListener){

        mListener = listener

    }
    //


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comm_card_rv_item, parent, false)
        return CustomViewHolder(view,mListener)//추가
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.title.text = CommList.get(position).Title
        holder.nickname.text = CommList.get(position).Nickname
        holder.contents.text = CommList.get(position).Contents

    }

    override fun getItemCount(): Int {
        return CommList.size
    }



    //listener 추가
    class CustomViewHolder (itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val nickname = itemView.findViewById<TextView>(R.id.tvNickName)
        val contents = itemView.findViewById<TextView>(R.id.tvContents)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }


    }
}