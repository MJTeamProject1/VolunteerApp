package com.team1.volunteerapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(bannerList: ArrayList<Int>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = bannerList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.banner.setImageResource(item[position])
    }

    // itemClick 인터페이스
    interface ItemClick{
        fun onClick(view : View, position: Int)
    }
    var itemClick : ItemClick? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.banner_list_item, parent, false)) {

        val banner = itemView.findViewById<ImageView>(R.id.imageView_banner)!!
        init {
            itemView.setOnClickListener {

                if(position == 0){
                    //1365 홈페이지 접속
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1365.go.kr/vols/main.do"))
                    parent.context.startActivity(intent)
                }

                else if(position == 1){
                    //개발자 이메일 보내기
                    var emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rudtjr1206@naver.com", null))
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                    parent.context.startActivity(Intent.createChooser(emailIntent, ""))
                }
            }
        }
    }
}