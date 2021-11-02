package com.team1.volunteerapp.Favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.volunteerapp.R

class FavoriteRVAdapter (val items : MutableList<String>) : RecyclerView.Adapter<FavoriteRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.favorite_rv_item, parent, false)

        return ViewHolder(view1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(item : String){
            var rv_text = itemView.findViewById<TextView>(R.id.mRV_itemText_home)
            rv_text.text = item
        }
    }
}

