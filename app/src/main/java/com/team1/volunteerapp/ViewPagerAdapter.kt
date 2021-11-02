package com.team1.volunteerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(bannerList: ArrayList<Int>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = bannerList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = 2	// 아이템의 갯수를 임의로 확 늘린다.

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.banner.setImageResource(item[position])	// position에 3을 나눈 나머지 값을 사용한다.
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.banner_list_item, parent, false)) {

        val banner = itemView.findViewById<ImageView>(R.id.imageView_banner)!!
    }
}