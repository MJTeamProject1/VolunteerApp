package com.team1.volunteerapp.Comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.team1.volunteerapp.Favorite.FavoriteRVAdapter
import com.team1.volunteerapp.R

class CommentLVAdapter(val commentList : MutableList<CommentModel>) : BaseAdapter() {

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_item, parent, false)
        }

        val title = view?.findViewById<TextView>(R.id.commentrvTitle)
        val time = view?.findViewById<TextView>(R.id.commentrvTime)
        val nicknmae = view?.findViewById<TextView>(R.id.commentrvNickName)
        val thurmup = view?.findViewById<ImageView>(R.id.commentThumbBtn)

        title!!.text = commentList[position].commentTitle
        time!!.text = commentList[position].commentCreatedTime
        nicknmae!!.text = commentList[position].commentNickname

        return view!!
    }

}