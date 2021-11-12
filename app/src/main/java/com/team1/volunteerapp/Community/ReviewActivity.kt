package com.team1.volunteerapp.Community

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.team1.volunteerapp.R
import com.team1.volunteerapp.utils.FBRef
import kotlinx.android.synthetic.main.activity_home.*

class ReviewActivity : AppCompatActivity() {
    private val addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener =
        object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onShown(fab: FloatingActionButton?) {
                super.onShown(fab)
            }

            @SuppressLint("NewApi")
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
//                fab?.show()
            }
        }

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()
    private lateinit var commRVAdapter: CommAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setSupportActionBar(bottomAppBar)

        //Recycler view 연결
        val comm_rv = findViewById<RecyclerView>(R.id.rvComm2)

        commRVAdapter = CommAdapter(boardDataList)
        comm_rv.adapter = commRVAdapter
        comm_rv.layoutManager = LinearLayoutManager(this)

        commRVAdapter.itemClick = object : CommAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                //눌렀을때 어떻게 할지
                val intent = Intent(view.context,BoardInsideActivity::class.java)
                intent.putExtra("key",boardKeyList[position])
                intent.putExtra("review",true)
                startActivity(intent)
            }
        }

        // DB에서 데이터 받아오기기
        getFBBoardData()


    }
    private fun getFBBoardData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                boardDataList.clear()

                for(dataModel in snapshot.children){
                    Log.d("asvv",dataModel.toString())

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }

                boardDataList.reverse()
                boardKeyList.reverse()
                commRVAdapter.notifyDataSetChanged()
                Log.d("asvv",boardDataList.toString())

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        //key 값만 가져옴
        FBRef.reviewRef.addValueEventListener(postListener)
    }
}