package com.team1.volunteerapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.Community.BoardInsideActivity
import com.team1.volunteerapp.Community.BoardModel
import com.team1.volunteerapp.Community.CommAdapter
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBRef


class ReviewFragment : Fragment() {

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()
    private lateinit var commRVAdapter: CommAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_review, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.fragmentRV2)
        commRVAdapter = CommAdapter(boardDataList)
        rv.adapter = commRVAdapter
        rv.layoutManager = LinearLayoutManager(context)

        commRVAdapter.itemClick = object : CommAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                //눌렀을때 어떻게 할지
                val intent = Intent(view.context, BoardInsideActivity::class.java)
                intent.putExtra("key",boardKeyList[position])
                intent.putExtra("review",true)
                startActivity(intent)
            }
        }

        // DB에서 데이터 받아오기
        getFBBoardData()

        return view
    }

    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                boardDataList.clear()
                for(dataModel in snapshot.children){

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardDataList.reverse()
                boardKeyList.reverse()
                commRVAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        //key 값만 가져옴
        FBRef.reviewRef.addValueEventListener(postListener)
    }


}